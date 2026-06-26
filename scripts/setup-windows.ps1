<#
.SYNOPSIS
  One-shot Windows build environment setup for the Poultry Broiler Management app.

.DESCRIPTION
  Installs (admin-free, per-user) everything required to build the project:
    - Eclipse Temurin JDK 17 (ZIP distribution, extracted to a user-writable dir)
    - Android SDK cmdline-tools + platform-tools + platforms;android-35 + build-tools;35.0.0
    - Writes local.properties pointing Gradle at the SDK (gitignored)
    - Wires JAVA_HOME / ANDROID_HOME / ANDROID_SDK_ROOT / PATH
  Idempotent: re-running skips anything already present.

.PARAMETER SdkDir
  Where to place the Android SDK. Default: $env:LOCALAPPDATA\android-sdk

.PARAMETER JdkDir
  Where to extract the JDK. Default: $env:LOCALAPPDATA\jdk-17

.PARAMETER JdkBuild
  Temurin release build tag. Default: 17.0.19+10

.PARAMETER SkipJdk
  Skip JDK installation (assume a JDK 17 is already on PATH/JAVA_HOME).

.PARAMETER SkipSdk
  Skip Android SDK installation.

.PARAMETER PersistEnv
  Persist env vars for the current user via setx (in addition to this session).

.PARAMETER Build
  Run .\gradlew.bat assembleDevDebug after setup completes.

.EXAMPLE
  pwsh scripts/setup-windows.ps1
  pwsh scripts/setup-windows.ps1 -PersistEnv -Build
#>
[CmdletBinding()]
param(
    [string]$SdkDir = (Join-Path $env:LOCALAPPDATA 'android-sdk'),
    [string]$JdkDir = (Join-Path $env:LOCALAPPDATA 'jdk-17'),
    [string]$JdkBuild = '17.0.19+10',
    [switch]$SkipJdk,
    [switch]$SkipSdk,
    [switch]$PersistEnv,
    [switch]$Build
)

$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'

function Write-Step([string]$msg) { Write-Host "`n== $msg" -ForegroundColor Cyan }
function Write-Ok([string]$msg)   { Write-Host "   OK: $msg" -ForegroundColor Green }
function Write-Info([string]$msg) { Write-Host "   $msg" -ForegroundColor DarkGray }

function Test-JdkVersion {
    param([string]$JavaExe)
    if (-not (Test-Path $JavaExe)) { return $false }
    try {
        $out = & $JavaExe -version 2>&1 | Out-String
        return $out -match 'version "17\.'
    } catch { return $false }
}

function Install-Jdk {
    Write-Step 'JDK 17 (Eclipse Temurin, per-user)'
    $existing = Get-ChildItem $JdkDir -Directory -ErrorAction SilentlyContinue | Where-Object Name -like 'jdk-17*'
    $javaHome = $null
    if ($existing) { $javaHome = $existing[0].FullName }
    if ($javaHome -and (Test-JdkVersion (Join-Path $javaHome 'bin\java.exe'))) {
        Write-Ok "already installed at $javaHome"
        return $javaHome
    }

    if ($SkipJdk) { throw 'SkipJdk set but no usable JDK 17 found.' }

    $tag = $JdkBuild -replace '\+', '-'
    $ver = ($JdkBuild -replace '\+.*', '') -replace '_', '.'
    $url = "https://github.com/adoptium/temurin17-binaries/releases/download/jdk-$JdkBuild/OpenJDK17U-jdk_x64_windows_hotspot_$($ver)_$($tag.Split('-')[-1]).zip"
    $zip = Join-Path $env:TEMP "temurin17-$JdkBuild.zip"
    Write-Info "downloading $url"
    Invoke-WebRequest -Uri $url -OutFile $zip -UseBasicParsing -TimeoutSec 600

    New-Item -ItemType Directory -Force -Path $JdkDir | Out-Null
    Write-Info "extracting to $JdkDir"
    Expand-Archive -Path $zip -DestinationPath $JdkDir -Force
    Remove-Item $zip -Force

    $javaHome = (Get-ChildItem $JdkDir -Directory | Where-Object Name -like 'jdk-17*' | Select-Object -First 1).FullName
    if (-not (Test-JdkVersion (Join-Path $javaHome 'bin\java.exe'))) {
        throw "JDK extraction did not yield a working java.exe under $JdkDir"
    }
    Write-Ok "installed at $javaHome"
    return $javaHome
}

function Install-AndroidSdk {
    Write-Step 'Android SDK (cmdline-tools + platform-tools + android-35 + build-tools;35.0.0)'
    $toolsBin = Join-Path $SdkDir 'cmdline-tools\latest\bin'
    if (-not (Test-Path (Join-Path $toolsBin 'sdkmanager.bat'))) {
        if ($SkipSdk) { throw 'SkipSdk set but no sdkmanager found.' }

        $ctUrl = 'https://dl.google.com/android/repository/commandlinetools-win-11076708_latest.zip'
        $zip = Join-Path $env:TEMP 'commandlinetools.zip'
        Write-Info "downloading $ctUrl"
        Invoke-WebRequest -Uri $ctUrl -OutFile $zip -UseBasicParsing -TimeoutSec 600

        $staging = Join-Path $env:TEMP 'cmdline-tools-extract'
        if (Test-Path $staging) { Remove-Item $staging -Recurse -Force }
        Expand-Archive -Path $zip -DestinationPath $staging -Force
        Remove-Item $zip -Force

        # The archive ships a top-level "cmdline-tools" folder; sdkmanager expects it under "latest".
        $destLatest = Join-Path $SdkDir 'cmdline-tools\latest'
        New-Item -ItemType Directory -Force -Path (Split-Path $destLatest) | Out-Null
        if (Test-Path $destLatest) { Remove-Item $destLatest -Recurse -Force }
        Move-Item (Join-Path $staging 'cmdline-tools') $destLatest -Force
        Remove-Item $staging -Recurse -Force -ErrorAction SilentlyContinue
        Write-Ok "cmdline-tools installed"
    } else {
        Write-Ok 'cmdline-tools already present'
    }

    $sdkmanager = Join-Path $toolsBin 'sdkmanager.bat'
    $packages = @('platform-tools', 'platforms;android-35', 'build-tools;35.0.0')
    Write-Info 'accepting SDK licenses'
    $ys = ("y`n" * 20)
    $ys | & $sdkmanager --licenses 2>&1 | Out-Null

    foreach ($pkg in $packages) {
        Write-Info "ensuring $pkg"
        $ys | & $sdkmanager $pkg 2>&1 | Out-Null
    }
    Write-Ok "SDK components ready under $SdkDir"
    return $SdkDir
}

function Set-Environment {
    param(
        [string]$JavaHome,
        [string]$AndroidHome
    )
    Write-Step 'Environment variables'
    $env:JAVA_HOME = $JavaHome
    $env:ANDROID_HOME = $AndroidHome
    $env:ANDROID_SDK_ROOT = $AndroidHome
    $paths = @(
        (Join-Path $JavaHome 'bin'),
        (Join-Path $AndroidHome 'cmdline-tools\latest\bin'),
        (Join-Path $AndroidHome 'platform-tools')
    )
    foreach ($p in $paths) {
        if ($env:PATH -notlike "*$p*") { $env:PATH = "$p;$env:PATH" }
    }
    Write-Ok "session env set (JAVA_HOME, ANDROID_HOME, ANDROID_SDK_ROOT, PATH)"

    if ($PersistEnv) {
        setx JAVA_HOME $JavaHome | Out-Null
        setx ANDROID_HOME $AndroidHome | Out-Null
        setx ANDROID_SDK_ROOT $AndroidHome | Out-Null
        $userPath = [Environment]::GetEnvironmentVariable('PATH', 'User')
        foreach ($p in $paths) {
            if ($userPath -notlike "*$p*") { $userPath = "$p;$userPath" }
        }
        setx PATH $userPath | Out-Null
        Write-Ok 'persisted env vars for current user (open a new shell to pick them up)'
    }
}

function Write-LocalProperties {
    param([string]$AndroidHome)
    Write-Step 'local.properties'
    $props = Join-Path $PSScriptRoot '..\local.properties'
    $sdkLine = 'sdk.dir=' + ($AndroidHome -replace '\\', '\\')
    Set-Content -Path $props -Value $sdkLine -Encoding UTF8
    Write-Ok "wrote $((Resolve-Path $props).Path) (gitignored)"
}

# --- main ---
$repoRoot = Split-Path $PSScriptRoot -Parent
$javaHome = $null
if (-not $SkipJdk) { $javaHome = Install-Jdk }
elseif ($env:JAVA_HOME -and (Test-JdkVersion (Join-Path $env:JAVA_HOME 'bin\java.exe'))) { $javaHome = $env:JAVA_HOME }
else {
    $cmd = Get-Command java -ErrorAction SilentlyContinue
    if ($cmd) { $javaHome = Split-Path (Split-Path $cmd.Source -Parent) -Parent }
}
if (-not $javaHome) { throw 'No JDK 17 available. Drop -SkipJdk or install one first.' }

$androidHome = $SdkDir
if (-not $SkipSdk) { $androidHome = Install-AndroidSdk }
elseif (-not (Test-Path (Join-Path $SdkDir 'cmdline-tools\latest\bin\sdkmanager.bat'))) {
    Write-Info 'SkipSdk set and no existing SDK found; continuing with SdkDir as-is.'
}

Set-Environment -JavaHome $javaHome -AndroidHome $androidHome
Write-LocalProperties -AndroidHome $androidHome

if ($Build) {
    Write-Step 'Build (gradlew.bat assembleDevDebug)'
    & (Join-Path $repoRoot 'gradlew.bat') -p $repoRoot assembleDevDebug
}
else {
    Write-Host "`nDone. Build with:`n  .\gradlew.bat assembleDevDebug`n" -ForegroundColor Cyan
}
