# Git Auto-Commit Extension

This extension automatically stages and commits changes after Spec Kit commands
complete (`/speckit-specify`, `/speckit-plan`, `/speckit-tasks`,
`/speckit-implement`, `/speckit-iterate`).

## Why an extension?

Not every Spec Kit user wants automatic commits. Extracting this behavior into
a dedicated extension lets users:

- **Opt out** entirely by setting `auto_commit.default: false` and removing the
  per-event overrides in `git-config.yml`.
- **Enable per event** — e.g. commit after `/speckit-plan` but not after
  `/speckit-tasks`.
- **Customize the message** per event, using `%s` as a placeholder for the
  active feature slug (e.g. `feat(%s): implement tasks` → `feat(003-house-dimensions-wizard): implement tasks`).

## Commands

| Command | Description |
|---------|-------------|
| `speckit.git.commit` | Stage and commit all changes using the message configured for the triggering event. |

## Configuration

All configuration flows through `.specify/extensions/git/git-config.yml`:

```yaml
auto_commit:
  default: false          # Global toggle — set true to enable for all events

  after_specify:
    enabled: true
    message: "docs(spec): add specification for %s"

  after_plan:
    enabled: true
    message: "docs(plan): add implementation plan for %s"

  after_tasks:
    enabled: false

  after_implement:
    enabled: true
    message: "feat(%s): implement tasks"

  after_iterate:
    enabled: true
    message: "fix(%s): apply iteration fixes"
```

- `default` — global toggle applied when an event has no `enabled` key of its own.
- `<event>.enabled` — whether to auto-commit for that specific event. Overrides `default`.
- `<event>.message` — the commit message. `%s` is replaced with the active feature slug
  (derived from the most recently modified `specs/*/plan.md`, or the prerequisite check JSON).

### Events

| Event | Fires after |
|-------|-------------|
| `after_specify` | `/speckit-specify` |
| `after_plan` | `/speckit-plan` |
| `after_tasks` | `/speckit-tasks` |
| `after_implement` | `/speckit-implement` |
| `after_iterate` | `/speckit-iterate` |

## Requirements

- **Git** on PATH (the extension calls `git add`, `git commit`, `git status`).
- **Python 3** with **PyYAML** for config parsing (same requirement as the
  `agent-context` extension). Install with `pip install pyyaml` if missing.

## Graceful degradation

- Not a git repository → skip with a warning.
- No config file → skip (treated as `default: false`).
- Event disabled → skip.
- No uncommitted changes → skip.
- Python/PyYAML unavailable → skip with a warning.

No operation is destructive: the extension only ever runs `git add -A` +
`git commit`. It never pushes, force-pushes, resets, or amends.

## Manual invocation

```bash
# Bash (from repo root):
bash .specify/extensions/git/scripts/bash/auto-commit.sh after_implement

# PowerShell (from repo root):
pwsh .specify/extensions/git/scripts/powershell/auto-commit.ps1 -EventName after_implement
```

## Hooks

This extension declares optional hooks for all five events in `extension.yml`.
When `auto_execute_hooks: true` is set in `.specify/extensions.yml` (as it is in
this project) and the event is `enabled` in `git-config.yml`, commits happen
automatically. To make a hook mandatory instead of optional, set `optional: false`
in `.specify/extensions.yml` for that event.
