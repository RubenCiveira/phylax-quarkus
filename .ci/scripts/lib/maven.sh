#!/usr/bin/env bash
# =============================================================================
# maven.sh — Maven compiler driver
# SOURCE this file, do not execute it directly.
#
# Implements the standard compiler interface expected by lifecycle scripts:
#   get_version, set_version, clean, format, lint, test,
#   verify, sast, mutation, build
#
# To add a new compiler (e.g. gradle.sh), implement the same interface.
# The active driver is selected via COMPILER=maven in config.env.
#
# Expects in scope (sourced before this file):
#   - common.sh  (log, warn, err, MVN_CI_OPTS)
#   - config.env (REQUIRED_COVERAGE, NVD_API_KEY, FINAL_NAME...)
#
# Maven binary resolution order:
#   1. $MVN env var (explicit override)
#   2. ./mvnw in project root (standard Maven wrapper)
#   3. mvn from PATH (fallback)
# =============================================================================

_mvn_resolve() {
  if [ -n "${MVN:-}" ]; then return; fi
  if [ -x "./mvnw" ]; then
    MVN="./mvnw"
  elif command -v mvn >/dev/null 2>&1; then
    MVN="mvn"
  else
    err "No Maven binary found. Set \$MVN, provide ./mvnw, or install mvn."
  fi
}
_mvn_resolve
_mvn_resolve_java

# Profile XML fragments directory (sibling to this file)
_MVN_PROFILES_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)/maven"

# =============================================================================
# Public interface — compiler-agnostic names used by lifecycle scripts
# =============================================================================

# Prints the current project version to stdout (capture-friendly)
get_version() {
  $MVN help:evaluate -Dexpression=project.version -q -DforceStdout $MVN_CI_OPTS 2>/dev/null
}

# Sets the project version in all version descriptor files
# Usage: set_version <version>
set_version() {
  local version="${1:?set_version requires a version argument}"
  $MVN versions:set -DnewVersion="$version" -DgenerateBackupPoms=false -q $MVN_CI_OPTS >/dev/null 2>&1
}

# Prints the list of files modified by set_version, one per line.
# Used by release scripts to stage exactly the right files after a version bump.
# A different compiler driver (gradle.sh, npm.sh...) would list its own files here.
version_files() {
  echo "pom.xml"
}

clean() {
  log "Clean"
  $MVN clean -q $MVN_CI_OPTS
}

# Runs formatter:format + impsort:sort via injected CI profile
# Profile: .ci/scripts/lib/maven/format-profile.xml
format() {
  log "Format (formatter + impsort)"
  local tmp_pom
  tmp_pom="$(mktemp /tmp/format-pom-XXXXXX.xml)"
  _mvn_inject_profile "pom.xml" "$_MVN_PROFILES_DIR/format-profile.xml" "$tmp_pom"
  $MVN formatter:format impsort:sort -f "$tmp_pom" -Pci-format -q $MVN_CI_OPTS
  local result=$?
  rm -f "$tmp_pom"
  return $result
}

# Runs PMD static analysis + CPD duplicate detection
# Profile: .ci/scripts/lib/maven/pmd-profile.xml
lint() {
  log "Lint (PMD + CPD)"
  local tmp_pom
  tmp_pom="$(mktemp /tmp/pmd-pom-XXXXXX.xml)"
  _mvn_inject_profile "pom.xml" "$_MVN_PROFILES_DIR/pmd-profile.xml" "$tmp_pom"
  $MVN verify -f "$tmp_pom" -Pci-lint $MVN_CI_OPTS
  local result=$?
  rm -f "$tmp_pom"
  mkdir -p ./target/lint
  [ -f ./target/cpd.csv ] && mv ./target/cpd.csv ./target/lint/cpd.csv
  [ -f ./target/pmd.csv ] && mv ./target/pmd.csv ./target/lint/pmd.csv
  return $result
}

# Runs unit tests (fast, no coverage gate)
test() {
  log "Test (unit tests)"
  $MVN test $MVN_CI_OPTS
}

# Runs unit tests + JaCoCo coverage gate
# Reads: REQUIRED_COVERAGE (0-100, optional — defaults to 0)
# Profile: .ci/scripts/lib/maven/jacoco-profile.xml
verify() {
  log "Verify (tests + JaCoCo coverage)"
  local tmp_pom params=""
  tmp_pom="$(mktemp /tmp/jacoco-pom-XXXXXX.xml)"
  _mvn_inject_profile "pom.xml" "$_MVN_PROFILES_DIR/jacoco-profile.xml" "$tmp_pom"

  if [ -n "${REQUIRED_COVERAGE:-}" ]; then
    local min_cov
    min_cov="$(echo "scale=2; $REQUIRED_COVERAGE / 100" | bc)"
    log "Coverage gate: ${REQUIRED_COVERAGE}%"
    params="-Djacoco.min-coverage.instructions=$min_cov -Djacoco.min-coverage.branches=$min_cov"
  else
    warn "REQUIRED_COVERAGE not set — gate disabled (0.00)"
    params="-Djacoco.min-coverage.instructions=0.00 -Djacoco.min-coverage.branches=0.00"
  fi

  $MVN clean verify -f "$tmp_pom" -Pci-coverage $params $MVN_CI_OPTS
  local result=$?
  rm -f "$tmp_pom"
  mkdir -p ./target/verify
  [ -f ./target/site/jacoco/jacoco.csv ] && \
    mv ./target/site/jacoco/jacoco.csv ./target/verify/jacoco.csv
  return $result
}

# Runs OWASP dependency-check SAST scan
# Reads: NVD_API_KEY (optional, from .env)
# Profile: .ci/scripts/lib/maven/owasp-profile.xml
sast() {
  log "SAST (OWASP dependency-check)"
  local tmp_pom params=""
  tmp_pom="$(mktemp /tmp/sast-pom-XXXXXX.xml)"
  _mvn_inject_profile "pom.xml" "$_MVN_PROFILES_DIR/owasp-profile.xml" "$tmp_pom"

  if [ -n "${NVD_API_KEY:-}" ]; then
    log "Using NVD API key"
    params="-DnvdApiKey=$NVD_API_KEY"
  else
    warn "No NVD_API_KEY set — scan may be slower (rate-limited)"
  fi

  $MVN verify -f "$tmp_pom" -Pci-sast $params $MVN_CI_OPTS
  local result=$?
  rm -f "$tmp_pom"
  mkdir -p ./target/sast
  [ -f ./target/dependency-check-report.csv ] && \
    mv ./target/dependency-check-report.csv ./target/sast/dependency-check-report.csv
  return $result
}

# Deep quality validation via PIT mutation testing (slow — intended for nightly pipelines).
# Reads: REQUIRED_COVERAGE (mutation threshold 0-100, optional — defaults to 0)
# Profile: .ci/scripts/lib/maven/pit-profile.xml
deep_test() {
  log "Mutation test (PIT)"
  local tmp_pom params=""
  tmp_pom="$(mktemp /tmp/pit-pom-XXXXXX.xml)"
  _mvn_inject_profile "pom.xml" "$_MVN_PROFILES_DIR/pit-profile.xml" "$tmp_pom"

  if [ -n "${REQUIRED_COVERAGE:-}" ]; then
    log "Mutation threshold: ${REQUIRED_COVERAGE}%"
    params="-DmutationThreshold=$REQUIRED_COVERAGE"
  else
    warn "REQUIRED_COVERAGE not set — threshold disabled (0)"
    params="-DmutationThreshold=0"
  fi

  $MVN clean verify -f "$tmp_pom" -Pci-mutation $params $MVN_CI_OPTS
  local result=$?
  rm -f "$tmp_pom"
  mkdir -p ./target/mutation
  [ -f ./target/site/jacoco/jacoco.csv ] && \
    mv ./target/site/jacoco/jacoco.csv ./target/mutation/jacoco.csv
  return $result
}

# Compiles and packages the application (tests skipped)
# Reads: FINAL_NAME (optional — renames the output artifact in target/)
build() {
  log "Build (package, tests skipped)"
  $MVN clean package -DskipTests=true $MVN_CI_OPTS
  local result=$?

  if [ -n "${FINAL_NAME:-}" ] && [ "$result" -eq 0 ]; then
    local ext artifact
    ext="$($MVN help:evaluate -Dexpression=project.packaging -q -DforceStdout $MVN_CI_OPTS 2>/dev/null)"
    artifact="$($MVN help:evaluate -Dexpression=project.build.finalName -q -DforceStdout $MVN_CI_OPTS 2>/dev/null)"
    [ -f "./target/${artifact}.${ext}" ] && \
      mv "./target/${artifact}.${ext}" "./target/${FINAL_NAME}.${ext}"
  fi

  return $result
}

# =============================================================================
# Private helpers
# =============================================================================

# -----------------------------------------------------------------------------
# _mvn_required_java_version [pom-file]
#
# Reads the required Java major version from pom.xml without invoking Maven.
# Checks (in order): maven.compiler.release, maven.compiler.source, java.version
# Returns just the major version number (e.g. "17", "21").
# -----------------------------------------------------------------------------
_mvn_required_java_version() {
  local pom="${1:-pom.xml}"
  [ -f "$pom" ] || return 0
  grep -m1 -E '<(maven\.compiler\.release|maven\.compiler\.source|java\.version)>' "$pom" \
    | sed -E 's/.*>([0-9]+(\.[0-9]+)?)<.*/\1/' \
    | grep -oE '^[0-9]+' \
    | head -1
}

# -----------------------------------------------------------------------------
# _mvn_find_jdk <jdk-dir> <major-version>
#
# Searches <jdk-dir> for a JDK home matching <major-version>. Prints the path
# to the JDK home directory, or nothing if not found.
#
# Search order:
#   1. $jdk_dir/<version>           — explicit symlink/dir convention
#   2. $jdk_dir/jdk-<version>*.jdk/Contents/Home  — macOS .jdk bundles
#   3. $jdk_dir/jdk-<version>*      — Linux Temurin / Adoptium layout
#   4. $jdk_dir/java-<version>*     — Debian/Ubuntu openjdk layout
# -----------------------------------------------------------------------------
_mvn_find_jdk() {
  local dir="$1" version="$2" found=""

  # 1. Exact version subdirectory or symlink
  if [ -d "${dir}/${version}" ]; then
    echo "${dir}/${version}"; return
  fi

  # 2. macOS .jdk bundles (jdk-17.0.x.jdk/Contents/Home)
  found="$(find "$dir" -maxdepth 1 -type d -name "jdk-${version}*.jdk" 2>/dev/null \
    | sort | tail -1)"
  if [ -n "$found" ] && [ -d "${found}/Contents/Home" ]; then
    echo "${found}/Contents/Home"; return
  fi

  # 3. Linux: jdk-<version>* (Temurin, Adoptium, Oracle)
  found="$(find "$dir" -maxdepth 1 -type d -name "jdk-${version}*" 2>/dev/null \
    | sort | tail -1)"
  [ -n "$found" ] && { echo "$found"; return; }

  # 4. Linux: java-<version>* (Debian openjdk)
  found="$(find "$dir" -maxdepth 1 -type d -name "java-${version}*" 2>/dev/null \
    | sort | tail -1)"
  [ -n "$found" ] && echo "$found"
}

# -----------------------------------------------------------------------------
# _mvn_resolve_java
#
# Sets JAVA_HOME to the JDK that matches the version required by pom.xml.
# No-op when JAVA_HOME is already defined (caller's choice is respected).
#
# Resolution order:
#   1. JAVA_HOME already set                       → use as-is
#   2. JDK_DIR set → search there                  → export JAVA_HOME
#   3. SDKMAN  ~/.sdkman/candidates/java/<ver>.*   → export JAVA_HOME
#   4. macOS   /usr/libexec/java_home -v <ver>     → export JAVA_HOME
#   5. Nothing found                               → warn, use system Java
# -----------------------------------------------------------------------------
_mvn_resolve_java() {
  # 1. Already set — nothing to do
  [ -n "${JAVA_HOME:-}" ] && return

  local required_version
  required_version="$(_mvn_required_java_version)"

  if [ -z "$required_version" ]; then
    warn "Could not detect required Java version from pom.xml — using system Java"
    return
  fi

  local jdk_home=""

  # 2. Search JDK_DIR
  if [ -n "${JDK_DIR:-}" ]; then
    if [ -d "$JDK_DIR" ]; then
      jdk_home="$(_mvn_find_jdk "$JDK_DIR" "$required_version")"
    else
      warn "JDK_DIR is set but does not exist: $JDK_DIR"
    fi
  fi

  # 3. SDKMAN (~/.sdkman/candidates/java/<major>.<minor>.<patch>-<vendor>)
  if [ -z "$jdk_home" ]; then
    local sdkman_java_dir="${SDKMAN_DIR:-$HOME/.sdkman}/candidates/java"
    if [ -d "$sdkman_java_dir" ]; then
      jdk_home="$(find "$sdkman_java_dir" -maxdepth 1 -type d -name "${required_version}.*" \
        2>/dev/null | sort | tail -1)"
    fi
  fi

  # 4. macOS /usr/libexec/java_home fallback
  if [ -z "$jdk_home" ] && [ -x /usr/libexec/java_home ]; then
    jdk_home="$(/usr/libexec/java_home -v "${required_version}" 2>/dev/null)" || jdk_home=""
  fi

  # 4. Apply or warn
  if [ -n "$jdk_home" ]; then
    export JAVA_HOME="$jdk_home"
    log "JAVA_HOME → $JAVA_HOME (Java $required_version)"
  else
    warn "JDK $required_version not found — using system Java ($(java -version 2>&1 | head -1))"
  fi
}

# Injects a CI profile XML fragment into a temporary copy of pom.xml.
# The fragment must contain a <profile>...</profile> block.
# Usage: _mvn_inject_profile <source-pom> <profile-fragment> <output-pom>
_mvn_inject_profile() {
  local pom_file="$1"
  local profile_file="$2"
  local output_file="$3"

  [ -f "$profile_file" ] || err "Maven profile fragment not found: $profile_file"

  local profile_content
  profile_content="$(cat "$profile_file")"

  if grep -q "<profiles>" "$pom_file"; then
    sed "/<\/profiles>/i\\
$profile_content" "$pom_file" > "$output_file"
  else
    sed "/<\/project>/i\\
<profiles>$profile_content<\/profiles>" "$pom_file" > "$output_file"
  fi
}
