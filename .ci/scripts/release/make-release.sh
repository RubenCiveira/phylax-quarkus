#!/usr/bin/env bash
# =============================================================================
# make-release.sh — Create a release commit on the current branch
#
# Does NOT require a specific branch name. Version boundaries are tracked
# exclusively through chore(release): commits in git history.
#
# What it does:
#   1. Asserts workspace is clean
#   2. Bails out if HEAD is already a release commit (nothing to release)
#   3. Finds the last chore(release): commit as the previous version boundary
#   4. Detects semver increment type from commits since that boundary
#   5. Calculates next version
#   6. Updates project version via compiler driver (set_version)
#   7. Generates changelog section
#   8. Commits both changes with: chore(release): <version>
#
# Usage:
#   .ci/scripts/release/make-release.sh
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CI_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

source "$CI_DIR/env/config.env"
[ -f "$CI_DIR/env/.env" ] && source "$CI_DIR/env/.env"
source "$CI_DIR/scripts/lib/common.sh"
source "$CI_DIR/scripts/lib/${COMPILER}.sh"
source "$CI_DIR/scripts/lib/git.sh"
source "$CI_DIR/scripts/lib/changelog.sh"

require_cmd git

git_assert_clean_workspace

# Guard: nothing to do if HEAD is already a release commit
CURRENT_MSG="$(git log -1 --pretty=format:"%s")"
if [[ "$CURRENT_MSG" =~ ^chore\(release\): ]]; then
  ok "HEAD is already a release commit: $CURRENT_MSG"
  log "Nothing to release. Commit new changes before running make-release again."
  exit 0
fi

# Find the last release commit to use as the version baseline and increment range
LAST_RELEASE_COMMIT="$(git_last_release_commit)"

if [ -n "$LAST_RELEASE_COMMIT" ]; then
  LAST_RELEASE_MSG="$(git log -1 --pretty=format:"%s" "$LAST_RELEASE_COMMIT")"
  BASE_VERSION="$(echo "$LAST_RELEASE_MSG" | sed -E 's/^chore\(release\): //')"
  log "Last release: $BASE_VERSION ($(git rev-parse --short "$LAST_RELEASE_COMMIT"))"
  FROM_REF="$LAST_RELEASE_COMMIT"
else
  BASE_VERSION="$(get_version)"
  FROM_REF="$(git rev-list --max-parents=0 HEAD)"
  log "No previous release commit — using first commit as range base, version: $BASE_VERSION"
fi

INCREMENT="$(git_get_increment_type "$FROM_REF" "HEAD")"
log "Detected increment: $INCREMENT"

NEW_VERSION="$(git_bump_version "$BASE_VERSION" "$INCREMENT")"
log "Next version: $NEW_VERSION"

log "Updating version → $NEW_VERSION"
set_version "$NEW_VERSION"

log "Generating changelog for $NEW_VERSION"
changelog_generate "$NEW_VERSION"

mapfile -t _vfiles < <(version_files)
git add "${_vfiles[@]}" CHANGELOG.md
git commit -m "chore(release): ${NEW_VERSION}"

ok "Release created: $NEW_VERSION"
echo ""
log "Next steps:"
log "  git push"
log "  Open MR/PR: $(git branch --show-current) → <target-branch>"
