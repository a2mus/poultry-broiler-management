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

# --- Git on PATH? (git rev-parse above already confirms this) -------------

# --- No config -> default off --------------------------------------------
if [[ ! -f "$CONFIG_FILE" ]]; then
  echo "git: $CONFIG_FILE not found; auto-commit disabled by default." >&2
  exit 0
fi

# --- Parse YAML config via Python (preferring one with PyYAML) -----------
_python=""
_python_candidates=()
[[ -n "${SPECKIT_PYTHON:-}" ]] && _python_candidates+=("$SPECKIT_PYTHON")
_python_candidates+=("python3" "python")
for _candidate in "${_python_candidates[@]}"; do
  if command -v "$_candidate" >/dev/null 2>&1 \
    && "$_candidate" - <<'PY' >/dev/null 2>&1
import sys
try:
    import yaml  # noqa: F401
except ImportError:
    sys.exit(1)
sys.exit(0 if sys.version_info[0] == 3 else 1)
PY
  then
    _python="$_candidate"
    break
  fi
done
unset _candidate _python_candidates

if [[ -z "$_python" ]]; then
  echo "git: Python 3 with PyYAML not found on PATH; skipping auto-commit." >&2
  echo "  To resolve: pip install pyyaml" >&2
  exit 0
fi

# Emit "ENABLED MESSAGE" across two lines for the given event.
if ! _parsed="$("$_python" - "$CONFIG_FILE" "$EVENT_NAME" <<'PY'
import sys
import yaml

cfg_path, event = sys.argv[1], sys.argv[2]
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

# Coerce message to str
if not isinstance(message, str):
    message = ""

print("1" if enabled else "0")
print(message)
PY
)"; then
  echo "git: failed to parse $CONFIG_FILE; skipping auto-commit." >&2
  exit 0
fi

_enabled_line=""
_message_line=""
{
  read -r _enabled_line || true
  read -r _message_line || true
} <<< "$_parsed"
unset _parsed

# Strip a trailing CR (Python on Windows emits CRLF; "1\r" != "1").
_enabled_line="${_enabled_line%$'\r'}"
_message_line="${_message_line%$'\r'}"

if [[ "$_enabled_line" != "1" ]]; then
  echo "git: auto-commit disabled for event '$EVENT_NAME'; skipping." >&2
  exit 0
fi

# --- Resolve the active feature slug for %s substitution -----------------
# Prefer the JSON emitted by the prerequisite check; fall back to the
# specs/<feature> directory name with the most recently modified plan.md.
FEATURE_SLUG=""
if command -v pwsh >/dev/null 2>&1; then
  FEATURE_SLUG="$(pwsh -NoProfile -File "$PROJECT_ROOT/.specify/scripts/powershell/check-prerequisites.ps1" -Json 2>/dev/null \
    | "$_python" -c 'import json,sys
try:
    d=json.load(sys.stdin)
    fp=d.get("FEATURE_DIR") or d.get("featureDir") or ""
    import os
    print(os.path.basename(os.path.normpath(fp)) if fp else "")
except Exception:
    print("")
' 2>/dev/null || true)"
fi
if [[ -z "$FEATURE_SLUG" ]]; then
  FEATURE_SLUG="$("$_python" - "$PROJECT_ROOT" <<'PY'
import os, sys
from pathlib import Path
specs = Path(sys.argv[1]) / "specs"
plans = sorted(
    specs.glob("*/plan.md"),
    key=lambda p: p.stat().st_mtime,
    reverse=True,
)
if not plans:
    print("")
    sys.exit(0)
# plans[0] = specs/<slug>/plan.md -> parent name
print(plans[0].parent.name)
PY
)"
fi

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
if git diff --quiet HEAD -- && git diff --cached --quiet -- && [[ -z "$(git ls-files --others --exclude-standard)" ]]; then
  echo "git: no uncommitted changes; skipping auto-commit." >&2
  exit 0
fi

# --- Stage and commit -----------------------------------------------------
git add -A
# If, after staging, there is truly nothing to commit, bail.
if git diff --cached --quiet HEAD --; then
  echo "git: staged changes result in no diff; skipping auto-commit." >&2
  exit 0
fi

git commit -m "$COMMIT_MESSAGE" >/dev/null 2>&1 \
  && echo "git: committed ($EVENT_NAME) -> $COMMIT_MESSAGE" \
  || { echo "git: commit failed; see git output above." >&2; exit 1; }
