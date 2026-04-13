#!/usr/bin/env bash
# =============================================================================
# docker.sh — Docker operations library
# SOURCE this file, do not execute it directly.
#
# Expects in scope (sourced before this file):
#   - common.sh  (log, warn, err, ok, run_cmd, get_platform)
#   - config.env (IMAGE_REGISTRY, IMAGE_NAME)
#
# Multi-arch strategy:
#   Each arch (amd64, arm64) is built and pushed separately as:
#     <registry>/<image>:<version>-<arch>
#   Once both are published, a manifest list is created as:
#     <registry>/<image>:<version>
#
# This allows each CI runner to build its native arch in parallel,
# without requiring QEMU or cross-compilation.
# =============================================================================

# Architectures that compose the multi-arch manifest
DOCKER_ARCHS="${DOCKER_ARCHS:-amd64 arm64}"

# -----------------------------------------------------------------------------
# docker_build — Build image for the current platform
#
# Usage: docker_build <dockerfile-path> <version> [extra docker build args...]
#
# Produces: <registry>/<image>:<version>-<platform>
# -----------------------------------------------------------------------------
docker_build() {
  local dockerfile="${1:?docker_build requires a Dockerfile path}"
  local version="${2:?docker_build requires a version}"
  shift 2

  local platform
  platform="$(get_platform)"
  local tag="${IMAGE_REGISTRY}/${IMAGE_NAME}:${version}-${platform}"

  log "Building Docker image: $tag (platform: $platform)"
  require_cmd docker

  run_cmd docker build \
    --platform "linux/${platform}" \
    --progress=plain \
    --no-cache \
    --force-rm \
    -f "$dockerfile" \
    -t "$tag" \
    "$@" \
    .

  ok "Image built: $tag"
}

# -----------------------------------------------------------------------------
# docker_push — Login and push the current platform's image
#
# Usage: docker_push <version>
#
# Reads: REGISTRY_USER, REGISTRY_PASS (from .env)
# -----------------------------------------------------------------------------
docker_push() {
  local version="${1:?docker_push requires a version}"
  local platform
  platform="$(get_platform)"
  local tag="${IMAGE_REGISTRY}/${IMAGE_NAME}:${version}-${platform}"

  require_cmd docker

  log "Logging in to ${IMAGE_REGISTRY}"
  run_cmd docker login -u "${REGISTRY_USER:?REGISTRY_USER not set}" \
                       -p "${REGISTRY_PASS:?REGISTRY_PASS not set}" \
                       "${IMAGE_REGISTRY}"

  log "Pushing image: $tag"
  run_cmd docker push "$tag"
  ok "Pushed: $tag"
}

# -----------------------------------------------------------------------------
# docker_manifest — Create and push a multi-arch manifest list
#
# Call this after ALL platform images have been pushed.
# Idempotent: safe to run multiple times.
#
# Usage: docker_manifest <version>
# -----------------------------------------------------------------------------
docker_manifest() {
  local version="${1:?docker_manifest requires a version}"
  local manifest_tag="${IMAGE_REGISTRY}/${IMAGE_NAME}:${version}"

  require_cmd docker

  if ! docker_all_archs_published "$version"; then
    warn "Not all architectures published yet — skipping manifest creation"
    warn "Expected: ${manifest_tag}-{$(echo "$DOCKER_ARCHS" | tr ' ' ',')}"
    return 1
  fi

  log "Creating manifest: $manifest_tag"
  local amend_args=()
  for arch in $DOCKER_ARCHS; do
    amend_args+=(--amend "${manifest_tag}-${arch}")
  done

  run_cmd docker manifest create "$manifest_tag" "${amend_args[@]}"
  run_cmd docker manifest push "$manifest_tag"
  ok "Manifest published: $manifest_tag"
}

# -----------------------------------------------------------------------------
# docker_all_archs_published — Check all arch images exist in the registry
#
# Usage: docker_all_archs_published <version>
# Returns 0 if all arches are present, 1 otherwise.
# -----------------------------------------------------------------------------
docker_all_archs_published() {
  local version="${1:?docker_all_archs_published requires a version}"
  local base="${IMAGE_REGISTRY}/${IMAGE_NAME}:${version}"
  local all_found=true

  for arch in $DOCKER_ARCHS; do
    local image="${base}-${arch}"
    if docker manifest inspect "$image" >/dev/null 2>&1; then
      ok "Found: $image"
    else
      warn "Not found: $image"
      all_found=false
    fi
  done

  $all_found
}

# -----------------------------------------------------------------------------
# docker_run_local — Run the image locally with a local env file
#
# Usage: docker_run_local <version> [env-file]
# Defaults: env-file = src/main/docker/env.local/.env
# -----------------------------------------------------------------------------
docker_run_local() {
  local version="${1:?docker_run_local requires a version}"
  local env_file="${2:-src/main/docker/env.local/.env}"
  local tag="${IMAGE_REGISTRY}/${IMAGE_NAME}:${version}"

  require_cmd docker

  if [ ! -f "$env_file" ]; then
    err "Env file not found: $env_file"
  fi

  log "Running $tag with env file: $env_file"
  run_cmd docker run \
    --env-file "$env_file" \
    -i --rm \
    -p 8090:8090 \
    -p 9002:9002 \
    "$tag"
}
