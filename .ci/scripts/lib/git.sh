#!/usr/bin/env bash
# =============================================================================
# git.sh — Git flow helper library
# SOURCE this file, do not execute it directly.
#
# Expects in scope (sourced before this file):
#   - common.sh  (log, warn, err)
#   - config.env (MAIN_BRANCH, DEVELOP_BRANCH, CHANGELOG_INCLUDE_BODY)
# =============================================================================

# -----------------------------------------------------------------------------
# git_get_increment_type [<from-ref> [<to-ref>]]
#
# Analyses commits between two refs and returns the semver increment type:
#   major | minor | patch
#
# Defaults: from=MAIN_BRANCH, to=DEVELOP_BRANCH
#
# Rules (Conventional Commits):
#   BREAKING CHANGE in body or ! suffix → major
#   feat:                               → minor
#   fix: (or anything else)             → patch
#
# Release commits (chore(release):) are excluded from the analysis.
# -----------------------------------------------------------------------------
git_get_increment_type() {
  local from="${1:-$MAIN_BRANCH}"
  local to="${2:-$DEVELOP_BRANCH}"

  local commits
  commits="$(git log "${from}..${to}" --pretty=format:"%s" \
    | grep -v "^chore(release):" || true)"

  local has_breaking=false has_feat=false

  while IFS= read -r line; do
    [[ "$line" == *"BREAKING CHANGE"* || "$line" == *"!:"* ]] && has_breaking=true
    [[ "$line" =~ ^feat(\([^)]*\))?: ]] && has_feat=true
  done <<< "$commits"

  if $has_breaking; then echo "major"
  elif $has_feat;   then echo "minor"
  else                   echo "patch"
  fi
}

# -----------------------------------------------------------------------------
# git_bump_version <base-version> <increment-type>
#
# Prints the next semver version. Strips pre-release suffixes (e.g. -SNAPSHOT).
#
# Example:
#   git_bump_version 1.2.3 minor   → 1.3.0
#   git_bump_version 2.0.0-SNAPSHOT major → 3.0.0
# -----------------------------------------------------------------------------
git_bump_version() {
  local base="${1:?git_bump_version requires a base version}"
  local type="${2:?git_bump_version requires an increment type (major|minor|patch)}"

  # Strip pre-release suffix
  local core="${base%%-*}"
  IFS='.' read -r major minor patch <<< "$core"

  case "$type" in
    major) major=$((major + 1)); minor=0; patch=0 ;;
    minor) minor=$((minor + 1)); patch=0 ;;
    patch) patch=$((patch + 1)) ;;
    *)     err "Unknown increment type: $type. Use major, minor or patch" ;;
  esac

  echo "${major}.${minor}.${patch}"
}

# -----------------------------------------------------------------------------
# git_next_rc_number <base-version>
#
# Finds the next available RC number for a given version by inspecting
# remote release branches: release/<version>-rc.<N>
#
# Example:
#   Existing: origin/release/1.3.0-rc.1, origin/release/1.3.0-rc.2
#   git_next_rc_number 1.3.0  → 3
# -----------------------------------------------------------------------------
git_next_rc_number() {
  local base_version="${1:?git_next_rc_number requires a base version}"

  local existing
  existing="$(git branch -r \
    | grep "origin/release/${base_version}-rc\." \
    | sed -E 's|.*-rc\.([0-9]+).*|\1|' \
    | sort -n \
    | tail -n1)"

  if [ -z "$existing" ]; then
    echo 1
  else
    echo $((existing + 1))
  fi
}

# -----------------------------------------------------------------------------
# git_last_release_commit [<ref>]
#
# Returns the hash of the most recent chore(release): commit reachable from
# <ref> (default: HEAD), or an empty string if none found.
# -----------------------------------------------------------------------------
git_last_release_commit() {
  local ref="${1:-HEAD}"
  git log "$ref" --pretty=format:"%H %s" \
    | grep " chore(release):" \
    | head -1 \
    | cut -d' ' -f1
}

# -----------------------------------------------------------------------------
# git_assert_clean_workspace
#
# Fails if there are uncommitted changes.
# -----------------------------------------------------------------------------
git_assert_clean_workspace() {
  if [ -n "$(git status --porcelain)" ]; then
    err "Uncommitted changes detected. Commit or stash before continuing."
  fi
}

# -----------------------------------------------------------------------------
# git_version_from_branch <branch> — reads pom.xml version from a git ref
# without checking out the branch (safe for protected branches).
# -----------------------------------------------------------------------------
git_version_from_branch() {
  local branch="${1:?git_version_from_branch requires a branch name}"
  git show "${branch}:pom.xml" \
    | grep -m1 "<version>" \
    | sed -E 's|.*<version>(.*)</version>.*|\1|' \
    | head -1
}
