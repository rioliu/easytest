MAVEN_IMAGE := docker.1ms.run/library/maven:3.8-openjdk-8-slim
PODMAN      := podman
M2_CACHE    := $(HOME)/.easytest-m2

RUN := $(PODMAN) run --rm \
	-v $(CURDIR):/usr/src/app:Z \
	-v $(M2_CACHE):/root/.m2:Z \
	-w /usr/src/app \
	$(MAVEN_IMAGE)

.PHONY: build test clean

build:
	mkdir -p $(M2_CACHE)
	$(RUN) mvn clean install -DskipTests
	rm -rf $(M2_CACHE)

test:
	mkdir -p $(M2_CACHE)
	$(RUN) mvn test
	rm -rf $(M2_CACHE)

clean:
	mkdir -p $(M2_CACHE)
	$(RUN) mvn clean
	rm -rf $(M2_CACHE)
