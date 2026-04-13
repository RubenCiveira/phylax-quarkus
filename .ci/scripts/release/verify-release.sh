#!/usr/bin/env bash
# =============================================================================
# verify-release.sh — Verify the current branch is ready to merge as a release
#
# Checks:
#   1. Workspace is clean (no uncommitted or unstaged changes)
#   2. Last commit is a release commit: chore(release): X.Y.Z
#   3. Version in project descriptor matches the version declared in that commit
#   4. Base version is coherent with the target branch
#      (the expected next version given the commits merged since last release
#       must match the base version declared in the release commit)
#
# Intended as a pre-merge gate: run in CI on the release branch before
# allowing the MR/PR to proceed.
#
# Usage:
#   .ci/scripts/release/verify-release.sh [<target-branch>]
#
# Arguments:
#   target-branch  Branch this release will be merged into (default: MAIN_BRANCH)
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CI_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

source "$CI_DIR/env/config.env"
[ -f "$CI_DIR/env/.env" ] && source "$CI_DIR/env/.env"
source "$CI_DIR/scripts/lib/common.sh"
source "$CI_DIR/scripts/lib/${COMPILER}.sh"
source "$CI_DIR/scripts/lib/git.sh"

require_cmd git

TARGET_BRANCH="${1:-$MAIN_BRANCH}"

RELEASE_COMMIT_PATTERN="^chore\(release\): ([0-9]+\.[0-9]+\.[0-9]+)$"
ERRORS=0

# -----------------------------------------------------------------------------
# Check 1 — Clean workspace
# -----------------------------------------------------------------------------
log "Check 1: workspace is clean"
if [ -n "$(git status --porcelain)" ]; then
  err "Uncommitted changes detected. The release branch must be clean."
  ERRORS=$((ERRORS + 1))
else
  ok "Workspace is clean"
fi

# -----------------------------------------------------------------------------
# Check 2 — Last commit is a release commit
# -----------------------------------------------------------------------------
log "Check 2: last commit is a release commit"
LAST_MSG="$(git log -1 --pretty=format:"%s")"

if [[ "$LAST_MSG" =~ $RELEASE_COMMIT_PATTERN ]]; then
  COMMIT_VERSION="${BASH_REMATCH[1]}"
  ok "Last commit is a release commit: $LAST_MSG"
else
  warn "Last commit is not a release commit: '$LAST_MSG'"
  warn "Expected pattern: chore(release): X.Y.Z"
  ERRORS=$((ERRORS + 1))
fi

# -----------------------------------------------------------------------------
# Check 3 — Project version matches commit version
# -----------------------------------------------------------------------------
if [ "${COMMIT_VERSION:-}" ]; then
  log "Check 3: project version matches commit version ($COMMIT_VERSION)"
  PROJECT_VERSION="$(get_version)"
  if [ "$PROJECT_VERSION" = "$COMMIT_VERSION" ]; then
    ok "Project version matches: $PROJECT_VERSION"
  else
    warn "Version mismatch — project: '$PROJECT_VERSION', commit: '$COMMIT_VERSION'"
    ERRORS=$((ERRORS + 1))
  fi
else
  log "Check 3: skipped (no commit version to compare)"
fi

# -----------------------------------------------------------------------------
# Check 4 — Base version is coherent with the target branch
#
# Strategy:
#   a) Read the version currently on origin/<target-branch>
#   b) Determine the increment type from commits between that ref and HEAD~1
#      (HEAD~1 excludes the release commit itself, which is chore(release):)
#   c) Compute the expected next version via git_bump_version
#   d) The commit version must equal the expected next version
# -----------------------------------------------------------------------------
if [ "${COMMIT_VERSION:-}" ]; then
  log "Check 4: base version is coherent with $TARGET_BRANCH"

  log "Fetching origin/$TARGET_BRANCH"
  git fetch origin "$TARGET_BRANCH" --quiet 2>/dev/null || true

  TARGET_VERSION="$(git_version_from_branch "origin/${TARGET_BRANCH}")"
  log "Version on $TARGET_BRANCH: $TARGET_VERSION"

  INCREMENT="$(git_get_increment_type "origin/${TARGET_BRANCH}" "HEAD~1")"
  log "Detected increment from $TARGET_BRANCH..HEAD~1: $INCREMENT"

  EXPECTED_VERSION="$(git_bump_version "$TARGET_VERSION" "$INCREMENT")"
  log "Expected next version: $EXPECTED_VERSION"

  if [ "$COMMIT_VERSION" = "$EXPECTED_VERSION" ]; then
    ok "Version is coherent: $COMMIT_VERSION (increment=$INCREMENT from $TARGET_VERSION)"
  else
    warn "Version incoherence — release declares $COMMIT_VERSION, but commits on $TARGET_BRANCH imply $EXPECTED_VERSION (increment=$INCREMENT from $TARGET_VERSION)"
    ERRORS=$((ERRORS + 1))
  fi
else
  log "Check 4: skipped (no commit version to compare)"
fi

# -----------------------------------------------------------------------------
# Result
# -----------------------------------------------------------------------------
echo ""
if [ "$ERRORS" -eq 0 ]; then
  ok "Release branch is ready to merge."
else
  err "Release verification failed with $ERRORS error(s). Fix before merging."
fi
