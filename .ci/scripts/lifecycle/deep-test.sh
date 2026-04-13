#!/usr/bin/env bash
# Deep quality validation — slow, expensive, intended for nightly pipelines.
# The actual mechanism depends on the compiler driver (COMPILER in config.env).
# Maven: PIT mutation testing. Future drivers may use fuzzing, property-based, etc.
#
# REQUIRED_COVERAGE — quality threshold (0-100). Optional, defaults to 0.
# Override: REQUIRED_COVERAGE=80 .ci/scripts/lifecycle/deep-test.sh
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CI_DIR="$(cd "$SCRIPT_DIR/../.." && pwd)"

source "$CI_DIR/env/config.env"
[ -f "$CI_DIR/env/.env" ] && source "$CI_DIR/env/.env"
source "$CI_DIR/scripts/lib/common.sh"
source "$CI_DIR/scripts/lib/${COMPILER}.sh"

deep_test
