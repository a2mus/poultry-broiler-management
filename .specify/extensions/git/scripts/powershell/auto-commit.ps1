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

# --- Parse YAML config and resolve feature slug in one Python call -------
# Combine Python discovery, YAML parsing, and feature slug resolution into
# a single subprocess call for efficiency.
$enabled = $false
$message = ""
$featureSlug = ""

# Find Python and parse all config at once
$python = $null
foreach ($candidate in @($env:SPECKIT_PYTHON, "python3", "python")) {
    if (-not $candidate) { continue }
    try {
        $null = & $candidate -c "import sys, yaml; sys.exit(0 if sys.version_info[0]==3 else 1)" 2>$null
        if ($LASTEXITCODE -eq 0) { $python = $candidate; break }
    } catch { }
}

if (-not $python) {
    Write-Warning "git: Python 3 with PyYAML not found; skipping auto-commit."
    return
}

# Single Python script handles YAML parsing and feature slug resolution
$pyScript = @"
import sys, yaml
from pathlib import Path

cfg_path, event, project_root = sys.argv[1], sys.argv[2], sys.argv[3]

# Parse YAML config
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

# Emit enabled, message, and feature slug
print('1' if enabled else '0')
print(message if message else '')

# Resolve feature slug from most recently modified plan.md
specs_dir = Path(project_root) / 'specs'
plans = sorted(
    specs_dir.glob('*/plan.md'),
    key=lambda p: p.stat().st_mtime,
    reverse=True,
) if specs_dir.exists() else []
feature_slug = plans[0].parent.name if plans else ''
print(feature_slug)
"@

$parsed = $pyScript | & $python - $ConfigFile $EventName $ProjectRoot 2>$null
if ($LASTEXITCODE -eq 0 -and $parsed) {
    $lines = $parsed -split "`n"
    if ($lines.Count -ge 1) { $enabled = ($lines[0].Trim() -eq "1") }
    if ($lines.Count -ge 2) { $message = $lines[1].Trim() }
    if ($lines.Count -ge 3) { $featureSlug = $lines[2].Trim() }
} else {
    Write-Warning "git: failed to parse config; skipping auto-commit."
    return
}

if (-not $enabled) {
    Write-Warning "git: auto-commit disabled for event '$EventName'; skipping."
    return
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
git commit -m $commitMessage *> $null
if ($LASTEXITCODE -eq 0) {
    Write-Host "git: committed ($EventName) -> $commitMessage"
} else {
    Write-Error "git: commit failed (exit $LASTEXITCODE)."
}
