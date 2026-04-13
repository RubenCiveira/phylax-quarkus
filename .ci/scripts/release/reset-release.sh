#!/usr/bin/env bash
# =============================================================================
# reset-release.sh — Remove release commits not present on a target branch
#
# Finds all chore(release): commits on the current branch that have NOT been
# merged into <target-branch> and removes them via git rebase.
#
# After the rebase, version descriptor files (pom.xml, etc.) and CHANGELOG.md
# are automatically reverted to their pre-release state — no manual cleanup
# needed.
#
# Safe to run before pushing. Do NOT run after pushing to a shared branch.
#
# Usage:
#   .ci/scripts/release/reset-release.sh <target-branch>
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CI_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

source "$CI_DIR/env/config.env"
[ -f "$CI_DIR/env/.env" ] && source "$CI_DIR/env/.env"
source "$CI_DIR/scripts/lib/common.sh"

require_cmd git

TARGET_BRANCH="${1:?reset-release requires a target branch (e.g. main or develop)}"

log "Fetching origin/$TARGET_BRANCH"
git fetch origin "$TARGET_BRANCH" --quiet 2>/dev/null || true

# Collect chore(release): commits on current branch not yet on target
RELEASE_HASHES=()
while IFS= read -r line; do
  hash="${line%% *}"
  [ -n "$hash" ] && RELEASE_HASHES+=("$hash")
done < <(git log "origin/${TARGET_BRANCH}..HEAD" --format="%H %s" \
  | grep " chore(release):" || true)

if [ "${#RELEASE_HASHES[@]}" -eq 0 ]; then
  ok "No local release commits to reset (all are already on $TARGET_BRANCH)"
  exit 0
fi

log "Found ${#RELEASE_HASHES[@]} release commit(s) to remove:"
for h in "${RELEASE_HASHES[@]}"; do
  git log -1 --format="  %h %s" "$h"
done

# Map full hashes to short hashes for the rebase todo file
SHORT_HASHES=()
for h in "${RELEASE_HASHES[@]}"; do
  SHORT_HASHES+=("$(git rev-parse --short "$h")")
done

# Write a minimal GIT_SEQUENCE_EDITOR script that drops the target commits.
# Uses sed -i.bak (portable: both macOS BSD sed and GNU sed support this form).
REBASE_EDITOR="$(mktemp /tmp/rebase-editor-XXXXXX.sh)"
cat > "$REBASE_EDITOR" << 'EOF'
#!/usr/bin/env sh
# GIT_SEQUENCE_EDITOR — drops commits listed in DROP_HASHES env var
TODOFILE="$1"
for hash in $DROP_HASHES; do
  sed -i.bak "s/^pick $hash /drop $hash /" "$TODOFILE"
done
rm -f "${TODOFILE}.bak"
EOF
chmod +x "$REBASE_EDITOR"

log "Rebasing to remove release commits..."
export DROP_HASHES="${SHORT_HASHES[*]}"
GIT_SEQUENCE_EDITOR="$REBASE_EDITOR" git rebase -i "origin/${TARGET_BRANCH}" || {
  rm -f "$REBASE_EDITOR"
  err "Rebase did not complete cleanly. Resolve conflicts, then: git rebase --continue"
}
rm -f "$REBASE_EDITOR"

ok "Reset complete — ${#RELEASE_HASHES[@]} release commit(s) removed from current branch."
