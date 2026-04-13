#!/usr/bin/env bash
# =============================================================================
# docker-run.sh — Run the application image locally
#
# Reads version from pom.xml. Uses the local env file for configuration.
#
# Usage:
#   .ci/artifact/docker-run.sh
#   .ci/artifact/docker-run.sh 1.2.3                        # specific version
#   ENV_FILE=src/main/docker/env.local/.env .ci/artifact/docker-run.sh
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

VERSION="${1:-$(get_version)}"
ENV_FILE="${ENV_FILE:-src/main/docker/env.local/.env}"

docker_run_local "$VERSION" "$ENV_FILE"
