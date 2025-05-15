#!/bin/bash

source "$SCRIPT_DIR/artifact/maven-lib.sh"

run() {
	:
}

init() {
   mvn_run_init
   retur $?
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
	mvn_format_formatter
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

package() {
	:
}
