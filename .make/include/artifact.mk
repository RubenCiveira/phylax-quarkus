run:
	@.make/runnable/run.sh run

doc-%:
	@.make/runnable/run.sh doc $* $(filter-out $@,$(MAKECMDGOALS))
	
site:
	@.make/runnable/run.sh site $* $(filter-out $@,$(MAKECMDGOALS))

clean:
	@.make/runnable/run.sh clean

format:
	@.make/runnable/run.sh format

lint:
	@.make/runnable/run.sh lint

sast:
	@.make/runnable/run.sh sast

verify:
	@.make/runnable/run.sh verify

test:
	@.make/runnable/run.sh test

build:
	@.make/runnable/run.sh build

deploy:
	@.make/runnable/run.sh deploy
