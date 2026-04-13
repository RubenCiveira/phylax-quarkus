#!/usr/bin/env bash
# =============================================================================
# init.sh — Bootstrap the CI scripts for this repository
#
# Run once after cloning, or after adding new scripts to .ci/
#
# What it does:
#   1. Grants execute permissions to all .ci scripts
#   2. Installs git hooks via .ci/hooks/install.sh
#
# Usage:
#   .ci/scripts/init.sh
#   .ci/scripts/init.sh --dry-run
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CI_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

DRY_RUN=false
[ "${1:-}" = "--dry-run" ] && DRY_RUN=true

# -----------------------------------------------------------------------------
# 1. Grant execute permissions to all scripts
# -----------------------------------------------------------------------------
echo ""
echo "Setting execute permissions on .ci scripts..."

find "$CI_DIR/scripts" -name "*.sh" | sort | while read -r file; do
  if $DRY_RUN; then
    echo "  [DRY-RUN] chmod +x $file"
  else
    chmod +x "$file"
    echo "  [OK]      ${file#"$CI_DIR/"}"
  fi
done

# Also ensure hook files are executable (no .sh extension)
find "$CI_DIR/hooks" -type f | sort | while read -r file; do
  if $DRY_RUN; then
    echo "  [DRY-RUN] chmod +x $file"
  else
    chmod +x "$file"
    echo "  [OK]      ${file#"$CI_DIR/"}"
  fi
done

# -----------------------------------------------------------------------------
# 2. Install git hooks
# -----------------------------------------------------------------------------
echo ""
if $DRY_RUN; then
  "$CI_DIR/hooks/install.sh" --dry-run
else
  "$CI_DIR/hooks/install.sh"
fi
