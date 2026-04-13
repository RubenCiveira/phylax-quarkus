#!/usr/bin/env bash
# =============================================================================
# docker-publish.sh — Build, push, and (if all archs ready) publish manifest
#
# Multi-arch strategy: each CI runner builds and pushes its native arch.
# The manifest is created automatically once all archs are available.
#
# Reads: REGISTRY_USER, REGISTRY_PASS (from .ci/env/.env)
# Reads: DOCKERFILE, IMAGE_REGISTRY, IMAGE_NAME (from config.env)
#
# Usage:
#   .ci/artifact/docker-publish.sh
#
# GitLab CI example (parallel amd64 + arm64 jobs):
#   publish:amd64:
#     tags: [amd64-runner]
#     script: .ci/artifact/docker-publish.sh
#
#   publish:arm64:
#     tags: [arm64-runner]
#     script: .ci/artifact/docker-publish.sh
# =============================================================================
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CI_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

source "$CI_DIR/env/config.env"
[ -f "$CI_DIR/env/.env" ] && source "$CI_DIR/env/.env"
source "$CI_DIR/scripts/lib/common.sh"
source "$CI_DIR/scripts/lib/${COMPILER}.sh"
source "$CI_DIR/scripts/lib/docker.sh"

require_cmd docker

DOCKERFILE="${DOCKERFILE:?DOCKERFILE not set in config.env}"
VERSION="$(get_version)"

# 1. Build for current platform
docker_build "$DOCKERFILE" "$VERSION"

# 2. Push current platform's image
docker_push "$VERSION"

# 3. Try to publish multi-arch manifest (no-op if other arch not ready yet)
docker_manifest "$VERSION" || true
