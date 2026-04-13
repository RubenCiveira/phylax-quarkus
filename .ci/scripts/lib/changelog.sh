#!/usr/bin/env bash
# =============================================================================
# changelog.sh — Changelog generation library
# SOURCE this file, do not execute it directly.
#
# Expects in scope (sourced before this file):
#   - common.sh  (log, warn, err, ok, require_cmd)
#   - git.sh     (git_last_release_commit)
#   - config.env (CHANGELOG_INCLUDE_BODY)
#
# Strategy: uses chore(release): commits as version markers instead of git
# tags. This works in protected-branch workflows where tags may not exist yet.
#
# Public interface:
#   changelog_generate <version>   — section from last release commit to HEAD
#   changelog_rebuild              — full rebuild from all release commits
# =============================================================================

CHANGELOG_FILE="${CHANGELOG_FILE:-CHANGELOG.md}"

# -----------------------------------------------------------------------------
# changelog_generate <version>
#
# Finds the most recent chore(release): commit before HEAD, generates a
# changelog section for all commits since then, and prepends it to CHANGELOG.md.
# chore(release): commits are excluded from the output.
# -----------------------------------------------------------------------------
changelog_generate() {
  local version="${1:?changelog_generate requires a version}"
  require_cmd git

  # Find last release commit (search from HEAD~1 to exclude a release commit
  # that may have just been created at HEAD by make-release.sh)
  local last_release_hash
  last_release_hash="$(_changelog_last_release_commit "HEAD~1")"

  if [ -z "$last_release_hash" ]; then
    last_release_hash="$(git rev-list --max-parents=0 HEAD)"
    log "No previous release commit — using initial commit as base"
  else
    local last_release_msg
    last_release_msg="$(git log -1 --pretty=format:"%s" "$last_release_hash")"
    log "Previous release: $last_release_msg ($last_release_hash)"
  fi

  log "Generating changelog: ${last_release_hash}..HEAD → [${version}]"
  local block
  block="$(_changelog_read_commits "$last_release_hash" "HEAD" "$version")"
  _changelog_prepend "$block"
  ok "Changelog updated: $CHANGELOG_FILE"
}

# -----------------------------------------------------------------------------
# changelog_rebuild
#
# Rebuilds CHANGELOG.md from scratch using all chore(release): commits
# in chronological order as version boundaries.
# -----------------------------------------------------------------------------
changelog_rebuild() {
  require_cmd git

  local release_hashes release_versions
  mapfile -t release_hashes  < <(git log --pretty=format:"%H" --reverse | \
    while read -r h; do
      msg="$(git log -1 --pretty=format:"%s" "$h")"
      [[ "$msg" =~ ^chore\(release\): ]] && echo "$h"
    done)
  mapfile -t release_versions < <(git log --pretty=format:"%s" --reverse | \
    grep "^chore(release):" | sed -E 's/^chore\(release\): //')

  [ "${#release_hashes[@]}" -ge 1 ] || err "No chore(release): commits found in history"

  log "Rebuilding $CHANGELOG_FILE from ${#release_hashes[@]} release commits"
  rm -f "$CHANGELOG_FILE"

  local first_commit
  first_commit="$(git rev-list --max-parents=0 HEAD)"

  local i
  for (( i=0; i < ${#release_hashes[@]}; i++ )); do
    local from_ref
    if [ "$i" -eq 0 ]; then
      from_ref="$first_commit"
    else
      from_ref="${release_hashes[$((i-1))]}"
    fi
    local block
    block="$(_changelog_read_commits "$from_ref" "${release_hashes[$i]}" "${release_versions[$i]}")"
    _changelog_prepend "$block"
  done

  ok "Changelog rebuilt: $CHANGELOG_FILE (${#release_hashes[@]} releases)"
}

# =============================================================================
# Private helpers
# =============================================================================

# Find the most recent chore(release): commit reachable from <ref>
_changelog_last_release_commit() {
  git_last_release_commit "${1:-HEAD}"
}

_changelog_prepend() {
  local content="$1"
  if [ -f "$CHANGELOG_FILE" ]; then
    local tmp
    tmp="$(mktemp)"
    { echo "$content"; echo; cat "$CHANGELOG_FILE"; } > "$tmp"
    mv "$tmp" "$CHANGELOG_FILE"
  else
    echo "$content" > "$CHANGELOG_FILE"
  fi
}

# Parses git log between two refs and returns a CHANGELOG.md section.
# Skips chore(release): commits — they are markers, not content.
_changelog_read_commits() {
  local from_ref="$1" to_ref="$2" version="$3"
  local include_body="${CHANGELOG_INCLUDE_BODY:-false}"

  local commits
  commits="$(git log "${from_ref}..${to_ref}" --no-merges \
    --pretty=format:"%h%n%s%n%b%n==END==")"

  [ -n "$commits" ] || { warn "No commits between $from_ref and $to_ref"; return 0; }

  local breaking="" added="" fixed="" changed="" removed="" other=""
  local current_hash="" current_title="" current_body=""

  while IFS= read -r line; do
    if [ "$line" = "==END==" ]; then
      # Skip release marker commits
      if [[ "$current_title" =~ ^chore\(release\): ]]; then
        current_hash="" current_title="" current_body=""
        continue
      fi

      local body_formatted=""
      if [ "$include_body" = "true" ] && [ -n "$current_body" ]; then
        local trimmed
        trimmed="$(echo "$current_body" | sed -e 's/^[[:space:]]*//' -e 's/[[:space:]]*$//')"
        if [ -n "${trimmed//[$'\n\r']/}" ]; then
          body_formatted=$'\n\n'
          while IFS= read -r body_line; do body_formatted+="> ${body_line}"$'\n'; done <<< "$trimmed"
        fi
      fi

      local breaking_line
      breaking_line="$(echo "$current_body" | grep -E "^BREAKING CHANGE:" || true)"
      if [ -n "$breaking_line" ]; then
        local msg="${breaking_line#BREAKING CHANGE:}"
        msg="$(echo "$msg" | sed -E 's/^[[:space:]]*//;s/[[:space:]]*$//' \
          | awk '{print toupper(substr($0,1,1)) substr($0,2)}')"
        breaking+="- ${msg} \`(${current_hash})\`"$'\n'
      fi

      local trimmed_msg uc_msg entry
      trimmed_msg="$(echo "${current_title#*:}" | sed -E 's/^[[:space:]]*//')"
      uc_msg="$(echo "$trimmed_msg" | awk '{print toupper(substr($0,1,1)) substr($0,2)}')"
      entry="- ${uc_msg} \`(${current_hash})\`${body_formatted}"$'\n'

      if   [[ "$current_title" =~ ^feat(\([^)]*\))?: ]];             then added+="$entry"
      elif [[ "$current_title" =~ ^fix(\([^)]*\))?: ]];              then fixed+="$entry"
      elif [[ "$current_title" =~ ^(chore|refactor)(\([^)]*\))?: ]]; then changed+="$entry"
      elif [[ "$current_title" =~ ^(remove|removed)(\([^)]*\))?: ]]; then removed+="$entry"
      else                                                                 other+="$entry"
      fi

      current_hash="" current_title="" current_body=""
    elif [ -z "$current_hash" ];  then current_hash="$line"
    elif [ -z "$current_title" ]; then current_title="$line"
    else                               current_body+="${line}"$'\n'
    fi
  done <<< "$commits"

  local date_str block
  date_str="$(date +%Y-%m-%d)"
  block="## [${version}] - ${date_str}"$'\n'
  [ -n "$breaking" ] && block+=$'\n'"### 💥 Breaking Changes"$'\n'"${breaking}"
  [ -n "$added" ]    && block+=$'\n'"### ✨ Added"$'\n'"${added}"
  [ -n "$fixed" ]    && block+=$'\n'"### 🐛 Fixed"$'\n'"${fixed}"
  [ -n "$changed" ]  && block+=$'\n'"### ♻️ Changed"$'\n'"${changed}"
  [ -n "$removed" ]  && block+=$'\n'"### 🔥 Removed"$'\n'"${removed}"
  [ -n "$other" ]    && block+=$'\n'"### 🧩 Other"$'\n'"${other}"
  block+=$'\n'
  echo "$block"
}
