import os

# 1. Update index.js
index_path = '/home/mus/.nvm/versions/node/v22.22.2/lib/node_modules/@getpaseo/cli/dist/commands/worktree/index.js'
content = open(index_path).read()

target_ls = 'worktree.command("ls").description("List Paseo-managed git worktrees")'
replacement_ls = 'worktree.command("ls").description("List Paseo-managed git worktrees").option("--cwd <path>", "Repository directory (default: current)")'
if target_ls in content:
    content = content.replace(target_ls, replacement_ls)
else:
    print("Warning: target_ls not found")

target_archive = '.argument("<name>", "Worktree name or branch name")'
replacement_archive = '.argument("<name>", "Worktree name or branch name").option("--cwd <path>", "Repository directory (default: current)")'
if target_archive in content:
    content = content.replace(target_archive, replacement_archive)
else:
    print("Warning: target_archive not found")

with open(index_path, 'w') as f:
    f.write(content)
print("Updated index.js")

# 2. Update ls.js
ls_path = '/home/mus/.nvm/versions/node/v22.22.2/lib/node_modules/@getpaseo/cli/dist/commands/worktree/ls.js'
content = open(ls_path).read()
target_ls_call = 'client.getPaseoWorktreeList({})'
replacement_ls_call = 'client.getPaseoWorktreeList({ cwd: options.cwd ?? process.cwd() })'
if target_ls_call in content:
    content = content.replace(target_ls_call, replacement_ls_call)
else:
    print("Warning: target_ls_call not found")
with open(ls_path, 'w') as f:
    f.write(content)
print("Updated ls.js")

# 3. Update archive.js
archive_path = '/home/mus/.nvm/versions/node/v22.22.2/lib/node_modules/@getpaseo/cli/dist/commands/worktree/archive.js'
content = open(archive_path).read()
target_archive_call = 'client.getPaseoWorktreeList({})'
replacement_archive_call = 'client.getPaseoWorktreeList({ cwd: options.cwd ?? process.cwd() })'
if target_archive_call in content:
    content = content.replace(target_archive_call, replacement_archive_call)
else:
    print("Warning: target_archive_call not found")
with open(archive_path, 'w') as f:
    f.write(content)
print("Updated archive.js")
