#!/usr/bin/env bash
# =============================================================================
# docker-build.sh — Build Docker image for the current platform
#
# Reads version from pom.xml automatically.
# Produces: <registry>/<image>:<version>-<platform>
#
# Usage:
#   .ci/artifact/docker-build.sh                  # native image (default)
#   DOCKERFILE=src/main/docker/Dockerfile.jvm .ci/artifact/docker-build.sh
#
# GitLab CI / GitHub Actions:
#   script:
#     - .ci/artifact/docker-build.sh
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

docker_build "$DOCKERFILE" "$VERSION"
