#!/usr/bin/env bash
# auto-commit.sh
#
# Stage and commit all changes after a Spec Kit command completes, using the
# message configured for the triggering event in:
#   .specify/extensions/git/git-config.yml
#
# Usage: auto-commit.sh <event_name> [message_override]
#
# <event_name> is the hook that triggered the commit, e.g. after_implement.
# When <message_override> is supplied, it takes precedence over the configured
# message for the event.
#
# Graceful degradation:
#   - Not a git repo            -> skip with a warning.
#   - No config file            -> skip (treated as default: false).
#   - Event disabled / default off -> skip.
#   - No uncommitted changes    -> skip.
#   - Git not on PATH           -> skip with a warning.

set -euo pipefail

EVENT_NAME="${1:-}"
MESSAGE_OVERRIDE="${2:-}"

if [[ -z "$EVENT_NAME" ]]; then
  echo "git: no event name supplied; usage: auto-commit.sh <event_name> [message]" >&2
  exit 0
fi

PROJECT_ROOT="$(pwd)"
CONFIG_FILE="$PROJECT_ROOT/.specify/extensions/git/git-config.yml"

# --- Not a git repo? ------------------------------------------------------
if ! git rev-parse --git-dir >/dev/null 2>&1; then
  echo "git: not a git repository; skipping auto-commit." >&2
  exit 0
fi

# --- No config -> default off --------------------------------------------
if [[ ! -f "$CONFIG_FILE" ]]; then
  echo "git: $CONFIG_FILE not found; auto-commit disabled by default." >&2
  exit 0
fi

# --- Find Python and parse all config in a single invocation -------------
# Combine Python discovery, YAML parsing, and feature slug resolution into
# one subprocess call for efficiency.
_python=""
_python_candidates=()
[[ -n "${SPECKIT_PYTHON:-}" ]] && _python_candidates+=("$SPECKIT_PYTHON")
_python_candidates+=("python3" "python")

if ! _output="$(
  for _candidate in "${_python_candidates[@]}"; do
    if command -v "$_candidate" >/dev/null 2>&1; then
      _python="$_candidate"
      break
    fi
  done
  if [[ -z "$_python" ]]; then
    echo "MISSING:Python 3 with PyYAML not found; pip install pyyaml" >&2
    exit 1
  fi
  "$_python" - "$CONFIG_FILE" "$EVENT_NAME" "$PROJECT_ROOT" <<'PY'
import sys, yaml, json, os
from pathlib import Path

cfg_path, event, project_root = sys.argv[1], sys.argv[2], sys.argv[3]

# Parse YAML config
try:
    with open(cfg_path, "r", encoding="utf-8") as fh:
        data = yaml.safe_load(fh) or {}
except Exception:
    data = {}

ac = data.get("auto_commit") or {}
default_enabled = bool(ac.get("default", False))
event_cfg = ac.get(event) or {}
if isinstance(event_cfg, dict):
    enabled = bool(event_cfg.get("enabled", default_enabled))
    message = event_cfg.get("message", "")
else:
    enabled = default_enabled
    message = ""

if not isinstance(message, str):
    message = ""

# Emit enabled status and message
print("1" if enabled else "0")
print(message if message else "")

# Resolve feature slug for %s substitution
feature_slug = ""
specs_dir = Path(project_root) / "specs"
plans = sorted(
    specs_dir.glob("*/plan.md"),
    key=lambda p: p.stat().st_mtime,
    reverse=True,
) if specs_dir.exists() else []
if plans:
    feature_slug = plans[0].parent.name

print(feature_slug)
PY
)"; then
  if [[ "$_output" == MISSING:* ]]; then
    echo "git: ${_output#MISSING:}" >&2
  else
    echo "git: failed to parse config; skipping auto-commit." >&2
  fi
  exit 0
fi

_enabled_line=""
_message_line=""
_feature_slug_line=""
{
  read -r _enabled_line || true
  read -r _message_line || true
  read -r _feature_slug_line || true
} <<< "$_output"

# Strip trailing CR (Python on Windows emits CRLF)
_enabled_line="${_enabled_line%$'\r'}"
_message_line="${_message_line%$'\r'}"
_feature_slug_line="${_feature_slug_line%$'\r'}"

if [[ "$_enabled_line" != "1" ]]; then
  echo "git: auto-commit disabled for event '$EVENT_NAME'; skipping." >&2
  exit 0
fi

FEATURE_SLUG="$_feature_slug_line"

# --- Compose the commit message ------------------------------------------
if [[ -n "$MESSAGE_OVERRIDE" ]]; then
  COMMIT_MESSAGE="$MESSAGE_OVERRIDE"
else
  COMMIT_MESSAGE="$_message_line"
fi
# Substitute %s with the feature slug (no-op if no %s present / no slug)
COMMIT_MESSAGE="${COMMIT_MESSAGE//\%s/$FEATURE_SLUG}"

# Empty message is not allowed by git; bail gracefully.
if [[ -z "$COMMIT_MESSAGE" ]]; then
  echo "git: no commit message configured for event '$EVENT_NAME'; skipping." >&2
  exit 0
fi

# --- Anything to commit? -------------------------------------------------
# Single git status check replaces three separate git commands
if ! git status --porcelain 2>/dev/null | grep -q .; then
  echo "git: no uncommitted changes; skipping auto-commit." >&2
  exit 0
fi

# --- Stage and commit -----------------------------------------------------
git add -A
git commit -m "$COMMIT_MESSAGE" >/dev/null 2>&1 \
  && echo "git: committed ($EVENT_NAME) -> $COMMIT_MESSAGE" \
  || { echo "git: commit failed; see git output above." >&2; exit 1; }
