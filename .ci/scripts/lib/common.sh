#!/usr/bin/env bash
# =============================================================================
# common.sh — Shared primitives: logging, colors, CI detection, platform, run_cmd
# SOURCE this file, do not execute it directly.
# =============================================================================

# -----------------------------------------------------------------------------
# Color support
# Disabled when: NO_COLOR is set, or output is not a TTY (CI pipelines, pipes)
# Spec: https://no-color.org
# -----------------------------------------------------------------------------
if [ -n "${NO_COLOR:-}" ] || [ ! -t 2 ]; then
  _C_RED=""; _C_GREEN=""; _C_YELLOW=""; _C_BLUE=""; _C_RESET=""
else
  _C_RED="\033[0;31m"
  _C_GREEN="\033[0;32m"
  _C_YELLOW="\033[1;33m"
  _C_BLUE="\033[0;34m"
  _C_RESET="\033[0m"
fi

# -----------------------------------------------------------------------------
# Logging — always to stderr so stdout stays clean for captured output
# -----------------------------------------------------------------------------
log()  { printf "${_C_BLUE}[INFO]${_C_RESET}  %s\n"  "$*" >&2; }
ok()   { printf "${_C_GREEN}[OK]${_C_RESET}    %s\n"  "$*" >&2; }
warn() { printf "${_C_YELLOW}[WARN]${_C_RESET}  %s\n" "$*" >&2; }
err()  { printf "${_C_RED}[ERROR]${_C_RESET} %s\n"  "$*" >&2; exit 1; }

# -----------------------------------------------------------------------------
# CI detection
# GitHub Actions, GitLab CI, CircleCI and most runners set CI=true
# -----------------------------------------------------------------------------
IS_CI="${CI:-false}"

# Maven flags suitable for CI (no progress bars, non-interactive)
if [ "$IS_CI" = "true" ]; then
  MVN_CI_OPTS="--no-transfer-progress --batch-mode"
else
  MVN_CI_OPTS=""
fi

# Portable commit SHA and branch name — works locally and in CI
ci_commit_sha() {
  echo "${CI_COMMIT_SHA:-${GITHUB_SHA:-$(git rev-parse HEAD 2>/dev/null || echo "unknown")}}"
}

ci_branch_name() {
  echo "${CI_COMMIT_REF_NAME:-${GITHUB_REF_NAME:-$(git branch --show-current 2>/dev/null || echo "unknown")}}"
}

# -----------------------------------------------------------------------------
# Platform detection — returns amd64 or arm64
# -----------------------------------------------------------------------------
get_platform() {
  case "$(uname -m)" in
    x86_64)        echo "amd64" ;;
    arm64|aarch64) echo "arm64" ;;
    *)             echo "amd64" ;;
  esac
}

# -----------------------------------------------------------------------------
# Dry-run support
# Set DRY_RUN=true to print commands without executing them
# -----------------------------------------------------------------------------
DRY_RUN="${DRY_RUN:-false}"

run_cmd() {
  if [ "$DRY_RUN" = "true" ]; then
    printf "${_C_YELLOW}[DRY-RUN]${_C_RESET} %s\n" "$*" >&2
  else
    "$@"
  fi
}

# -----------------------------------------------------------------------------
# Dependency checks
# -----------------------------------------------------------------------------
require_cmd() {
  local cmd="$1"
  command -v "$cmd" >/dev/null 2>&1 || err "Required command not found: $cmd"
}
