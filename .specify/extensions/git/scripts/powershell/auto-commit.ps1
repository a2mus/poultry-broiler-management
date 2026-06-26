#!/usr/bin/env pwsh
<#
.SYNOPSIS
  Stage and commit all changes after a Spec Kit command completes.

.DESCRIPTION
  Reads .specify/extensions/git/git-config.yml for the auto_commit section,
  looks up the triggering event, and (if enabled) runs `git add -A` then
  `git commit -m "<message>"`. The `%s` placeholder in the message is
  replaced with the active feature slug.

  Graceful degradation mirrors the bash variant:
    - Not a git repo        -> skip with a warning.
    - No config file        -> skip (treated as default: false).
    - Event disabled        -> skip.
    - No uncommitted changes-> skip.
    - Git not on PATH       -> skip with a warning.

.PARAMETER EventName
  The hook that triggered the commit, e.g. after_implement.

.PARAMETER MessageOverride
  Optional commit message that takes precedence over the configured one.

.EXAMPLE
  pwsh auto-commit.ps1 -EventName after_implement
#>
[CmdletBinding()]
param(
    [Parameter(Position = 0, Mandatory = $true)]
    [string]$EventName,

    [Parameter(Position = 1)]
    [string]$MessageOverride
)

$ErrorActionPreference = "Stop"

$ProjectRoot = (Get-Location).Path
$ConfigFile  = Join-Path $ProjectRoot ".specify/extensions/git/git-config.yml"

function Test-GitAvailable {
    try { git rev-parse --git-dir *> $null; return $true }
    catch { return $false }
}

function Get-ConfigValue {
    param([AllowNull()][object]$Object, [Parameter(Mandatory)][string]$Key)
    if ($null -eq $Object) { return $null }
    if ($Object -is [System.Collections.IDictionary]) { return $Object[$Key] }
    $prop = $Object.PSObject.Properties[$Key]
    if ($prop) { return $prop.Value }
    return $null
}

# --- Not a git repo? ------------------------------------------------------
if (-not (Test-GitAvailable)) {
    Write-Warning "git: not a git repository; skipping auto-commit."
    return
}

# --- No config -> default off --------------------------------------------
if (-not (Test-Path $ConfigFile)) {
    Write-Warning "git: $ConfigFile not found; auto-commit disabled by default."
    return
}

# --- Parse YAML config ----------------------------------------------------
# Try PyYAML via python first (matches bash variant); fall back to a simple
# parser if python/PyYAML is unavailable.
$enabled = $false
$message = ""

$python = $null
foreach ($candidate in @($env:SPECKIT_PYTHON, "python3", "python")) {
    if (-not $candidate) { continue }
    try {
        $probe = & $candidate -c "import sys, yaml; sys.exit(0 if sys.version_info[0]==3 else 1)" 2>$null
        if ($LASTEXITCODE -eq 0) { $python = $candidate; break }
    } catch { }
}

if ($python) {
    # NOTE: the Python source MUST be piped to stdin (python - reads stdin).
    # A trailing here-string is an argument, not stdin, so pipe it explicitly.
    $pyScript = @"
import sys, yaml
cfg_path, event = sys.argv[1], sys.argv[2]
try:
    with open(cfg_path, 'r', encoding='utf-8') as fh:
        data = yaml.safe_load(fh) or {}
except Exception:
    data = {}
ac = data.get('auto_commit') or {}
default_enabled = bool(ac.get('default', False))
event_cfg = ac.get(event) or {}
if isinstance(event_cfg, dict):
    enabled = bool(event_cfg.get('enabled', default_enabled))
    message = event_cfg.get('message', '')
else:
    enabled = default_enabled
    message = ''
if not isinstance(message, str):
    message = ''
print('1' if enabled else '0')
print(message)
"@
    $parsed = $pyScript | & $python - $ConfigFile $EventName 2>$null
    if ($LASTEXITCODE -eq 0 -and $parsed) {
        $lines = $parsed -split "`n"
        if ($lines.Count -ge 1) { $enabled = ($lines[0].Trim() -eq "1") }
        if ($lines.Count -ge 2) { $message = $lines[1].Trim() }
    }
} else {
    Write-Warning "git: Python 3 with PyYAML not found; skipping auto-commit."
    return
}

if (-not $enabled) {
    Write-Warning "git: auto-commit disabled for event '$EventName'; skipping."
    return
}

# --- Resolve the active feature slug --------------------------------------
$featureSlug = ""
if (Test-Path "$ProjectRoot/.specify/scripts/powershell/check-prerequisites.ps1") {
    try {
        $json = & pwsh -NoProfile -File "$ProjectRoot/.specify/scripts/powershell/check-prerequisites.ps1" -Json 2>$null
        if ($json) {
            $obj = $json | ConvertFrom-Json -ErrorAction Stop
            $fp  = Get-ConfigValue $obj "FEATURE_DIR"
            if (-not $fp) { $fp = Get-ConfigValue $obj "featureDir" }
            if ($fp) { $featureSlug = ([System.IO.Path]::GetFileName($fp.TrimEnd('/\'))) }
        }
    } catch { }
}
if (-not $featureSlug) {
    $specsDir = Join-Path $ProjectRoot "specs"
    if (Test-Path $specsDir) {
        $latest = Get-ChildItem -Path $specsDir -Filter "plan.md" -Recurse -File -ErrorAction SilentlyContinue |
            Sort-Object LastWriteTime -Descending | Select-Object -First 1
        if ($latest) { $featureSlug = $latest.Directory.Name }
    }
}

# --- Compose the commit message ------------------------------------------
if ($MessageOverride) {
    $commitMessage = $MessageOverride
} else {
    $commitMessage = $message
}
$commitMessage = $commitMessage -replace '%s', $featureSlug
if (-not $commitMessage) {
    Write-Warning "git: no commit message configured for event '$EventName'; skipping."
    return
}

# --- Anything to commit? --------------------------------------------------
$status = git status --porcelain 2>&1
if (-not $status) {
    Write-Warning "git: no uncommitted changes; skipping auto-commit."
    return
}

# --- Stage and commit -----------------------------------------------------
git add -A *> $null
$stageStatus = git status --porcelain 2>&1
if (-not $stageStatus) {
    Write-Warning "git: staged changes result in no diff; skipping auto-commit."
    return
}

git commit -m $commitMessage *> $null
if ($LASTEXITCODE -eq 0) {
    Write-Host "git: committed ($EventName) -> $commitMessage"
} else {
    Write-Error "git: commit failed (exit $LASTEXITCODE)."
}
