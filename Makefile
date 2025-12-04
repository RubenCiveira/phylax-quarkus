-include .make/include/common.mk .make/include/gitflow.mk .make/include/artifact.mk

docker-build:
	docker build --no-cache --force-rm -f src/main/docker/Dockerfile.native-micro.multistage -t civi/phylax-api .

docker-run:
	docker run --env-file src/main/docker/env.local/.env -i --rm -p 8090:8090 -p 9002:9002 civi/phylax-api &

docker-build-jvm:
	docker build --no-cache --force-rm -f src/main/docker/Dockerfile.jvm.multistage -t civi/phylax-api .
