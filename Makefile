-include .make/include/common.mk .make/include/gitflow.mk .make/include/artifact.mk

docker-build:
	docker build --no-cache --force-rm -f src/main/docker/Dockerfile.native-micro.multistage -t civi/phylax-api .

docker-run:
	docker run --env-file src/main/docker/env.local/.env -i --rm -p 8090:8090 -p 9002:9002 civi/phylax-api &

docker-build-jvm:
	docker build --no-cache --force-rm -f src/main/docker/Dockerfile.jvm.multistage -t civi/phylax-api .

# V.1: SQL adapters against MariaDB, in a throwaway phylax_v1_* schema (never the .env database).
test-mariadb:
	@set -a && . ./.env && set +a && \
	V1_MARIADB_URL="jdbc:mariadb://$${DATASOURCE_HOST:-localhost}:$${DATASOURCE_PORT:-3306}" \
	V1_MARIADB_USER="$${DATASOURCE_USER}" V1_MARIADB_PASSWORD="$${DATASOURCE_PASS}" \
	mvn test -Dtest='*MariaDbTest' -Dsurefire.failIfNoSpecifiedTests=false

# V.2: OpenID Conformance Suite (Basic, Config, RP-Initiated y Back-Channel Logout OP); ver conformance/README.md.
conformance:
	./conformance/run.sh
