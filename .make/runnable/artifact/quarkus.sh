#!/bin/bash

source "$SCRIPT_DIR/artifact/maven-lib.sh"
source "$SCRIPT_DIR/artifact/docker-lib.sh"

function run() {
	$MVN clean verify quarkus:run
}

init() {
   mvn_run_init
   return $?
}

doc() {
	mvn_run_doc $1 ${2} $3
}

site() {
	mvn_run_site $1 ${2} $3
}

clean() {
	mvn_run_clean
	return $?
}

format() {
	mvn_run_format
	return $?
}

lint() {
	mvn_lint_pmd
	return $?
}

sast() {
	mvn_sast_owasp
	return $?
}

verify() {
	mvn_verify_jacoco
	return $?
}

test() {
	mvn_test_pit
	return $?
}

build() {
	docker_build src/main/docker/Dockerfile.native-micro.multistage
}

deploy() {
    docker_publish
	# docker login -u ${REGISTRY_USER} -p ${REGISTRY_PASS} ${IMAGE_REGISTRY} 
	# docker build --platform linux/${CURRENT_PLATFORM} --build-arg  --progress=plain --no-cache --force-rm -f src/main/docker/Dockerfile.native-micro.multistage -t "${IMAGE_REGISTRY}/${IMAGE_NAME}:${CURRENT_VERSION}-${CURRENT_PLATFORM}" --push .
}

