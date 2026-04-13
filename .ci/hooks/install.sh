#!/usr/bin/env bash
# =============================================================================
# install.sh — Install CI git hooks into .git/hooks/ via symlinks
#
# Symlinks keep hooks in sync with the repo automatically —
# no need to re-run this script after hook updates.
#
# Usage:
#   .ci/hooks/install.sh            # install all hooks
#   .ci/hooks/install.sh --dry-run  # show what would be installed
#
# Make target:
#   make hooks-install
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(git -C "$SCRIPT_DIR" rev-parse --show-toplevel)"
HOOKS_SRC="$SCRIPT_DIR/git"
HOOKS_DST="$REPO_ROOT/.git/hooks"

DRY_RUN=false
[ "${1:-}" = "--dry-run" ] && DRY_RUN=true

# Hooks to install: filename in .ci/hooks/git/ → filename in .git/hooks/
HOOKS=(
  commit-msg
  pre-commit
)

echo ""
echo "Installing git hooks from .ci/hooks/git/ → .git/hooks/"
echo ""

for hook in "${HOOKS[@]}"; do
  src="$HOOKS_SRC/$hook"
  dst="$HOOKS_DST/$hook"

  if [ ! -f "$src" ]; then
    echo "  [SKIP]    $hook  (source not found)"
    continue
  fi

  if $DRY_RUN; then
    echo "  [DRY-RUN] $dst → $src"
    continue
  fi

  # Backup existing hook if it is not already one of ours
  if [ -f "$dst" ] && [ ! -L "$dst" ]; then
    mv "$dst" "${dst}.bak"
    echo "  [BACKUP]  ${dst}.bak"
  fi

  ln -sf "$src" "$dst"
  chmod +x "$src"
  echo "  [OK]      $hook"
done

echo ""
if $DRY_RUN; then
  echo "Dry-run complete. Run without --dry-run to apply."
else
  echo "Hooks installed. They will stay in sync automatically."
fi
echo ""
