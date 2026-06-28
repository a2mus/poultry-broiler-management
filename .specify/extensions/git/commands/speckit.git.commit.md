---
name: speckit.git.commit
description: Stage and commit all changes after a Spec Kit command completes, using the message configured for the triggering event.
---

# speckit.git.commit

Automatically stage and commit all changes after a Spec Kit command completes.

## Usage

```bash
# Run as a hook (auto-detected event):
bash .specify/extensions/git/scripts/bash/auto-commit.sh <event_name>

# PowerShell:
pwsh .specify/extensions/git/scripts/powershell/auto-commit.ps1 <event_name>
```

`<event_name>` is the hook that triggered the commit — one of:
`after_specify`, `after_plan`, `after_tasks`, `after_implement`, `after_iterate`.

## Behavior

1. Determine the event name from the triggering hook.
2. Read `.specify/extensions/git/git-config.yml` → `auto_commit` section.
3. Look up the specific event key (`enabled` + `message`); fall back to `auto_commit.default`.
4. If enabled for the event AND there are uncommitted changes:
   - `git add -A`
   - `git commit -m "<message>"` (with `%s` substituted by the active feature slug).
5. Skip gracefully if disabled, nothing to commit, or not a git repo.

## Graceful degradation

- Not a git repository → skip with a warning.
- No config file → skip (treated as `default: false`).
- No uncommitted changes → skip with a message.
- Git not on PATH → skip with a warning.

See `.specify/extensions/git/git-config.yml` to enable/disable per event.
