# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

**easytest** is a Java automation test framework (Maven, Java 8, TestNG) providing ready-to-use abstractions for REST API testing, Selenium WebUI testing, SSH client testing, and database testing. It is intended to be used as a library or starter template — tests in `src/test/` are integration examples, not unit tests.

## Prerequisites

No local JDK or Maven install required. Builds run inside a container via [Podman](https://podman.io/). The `Makefile` uses `maven:3.8-openjdk-8-slim` pulled from `docker.1ms.run` (a Docker Hub mirror). The Maven local repo is temporarily cached at `~/.easytest-m2` during the build and automatically deleted afterwards.

## Build & Run Commands

```bash
make image   # build the local builder image (only needed once, or after Dockerfile changes)
make build   # compile and package, skip tests
make test    # run the full test suite
make clean   # clean build output
```

For running a specific test class or method directly:
```bash
podman run --rm \
  -v $(pwd):/usr/src/app:Z \
  -v /tmp/easytest-m2:/root/.m2:Z \
  -w /usr/src/app \
  easytest-builder \
  mvn test -Dtest=RESTAPITest                     # single class
  mvn test -Dtest=RESTAPITest#testPublicAPI1      # single method
```

Selenium tests (`WebDriverTest`) additionally require ChromeDriver/GeckoDriver binaries passed at runtime — they are commented out in `testNG.xml` by default:
```bash
mvn test -Dwebdriver.chrome.driver=/path/to/chromedriver -Dwebdriver.gecko.driver=/path/to/geckodriver
```

## Test Suite Configuration

`src/test/resources/testNG.xml` defines which test classes run. Only `RESTAPITest` is active by default; `WebDriverTest`, `SSHClientTest`, and `DatabaseTest` are commented out. Edit this file to enable/disable test classes.

The suite registers two TestNG listeners:
- `ExtentReportListener` — creates HTML report at `test-output/automation_test_report.html`
- `AbstractTestBase` — handles WebDriver lifecycle and screenshot capture on failure

## Architecture

### Core Abstractions (`src/main/java/com/rioliu/test/`)

**`base/AbstractTestBase`** — Central base class for all test classes. Extends `TestListenerAdapter` and acts as both a TestNG listener and a test base. Manages a per-test stack of `WebDriver` instances (stored as a `ITestResult` attribute), automatically closing all drivers after each test. On failure, takes screenshots automatically. Provides `chrome()`, `firefox()` factory methods.

**`config/TestContext`** — Singleton providing access to system properties, environment variables, and config file loading. Supports `.properties` files (Apache Commons Configuration2), YAML files via both Apache Commons (`YAMLConfiguration`) and SnakeYAML (POJO mapping with `loadYamlConfigObjectFromFile`).

**`logging/`** — Custom logging abstraction layer on top of SLF4J. The `Logger` interface has `ConsoleLogger` (SLF4J), `ReportLogger` (ExtentReports), and `CompositeLogger` (both simultaneously). `ScreenshotLogger` extends `ReportLogger` for embedding screenshots in reports. Always use `LoggerFactory.getCompositeLogger(ClassName.class)` in test classes to log to both console and report.

**`reporting/`** — Thread-safe ExtentReports integration. `ExtentReportManager` holds a `ConcurrentHashMap<Long, ExtentTest>` keyed by thread ID for parallel test support. `ExtentReportListener` is a TestNG `ITestListener` that drives the report lifecycle. The report name can be overridden via `-Dextent.report.name=...`.

**`selenium/`** — `BasePageObject` is the POM base class — extend it to build page objects. `ByJQuery` is a custom Selenium `By` locator that injects jQuery into the page if absent and uses jQuery selectors. `Utils` handles screenshot capture to disk.

**`rest/RestClientConfigHelper`** — Singleton wrapper around RestAssured global config (HTTPS relaxation, proxy settings, request/response logging filters).

### Test Infrastructure Notes

- **WebDriver paths** are no longer hardcoded. Pass via `-D` flags when running Selenium tests (see above).
- **`DatabaseTest`** expects a local MySQL container: `podman run --rm --name mysqldb -e MYSQL_ROOT_PASSWORD=welcome1 -p 3306:3306 -d mysql`
- **`SSHClientTest`** expects a local SSH container: `podman run -d -p 8888:22 --name test_sshd rastasheep/ubuntu-sshd:14.04`
- **`selenium.element.wait.timeoutInSecs`** system property controls default explicit wait timeout in `BasePageObject` (default: 60s).

### Config File Formats

- `.properties`: flat key=value, loaded via `TestContext.get().loadPropertiesFromFile(path)`
- `.yml`: loaded either as `YAMLConfiguration` (dot-notation access) or as a typed POJO via SnakeYAML — define a POJO matching the YAML structure (see `YamlConfig`/`Info` in `src/test/`)

## Publishing to Nexus

Start a local Nexus instance:
```bash
podman run -d -p 8081:8081 --name nexus sonatype/nexus:oss
```

Upload artifact (default credentials `admin:admin123`):
```bash
curl -v -u 'admin:admin123' --upload-file pom.xml http://localhost:8081/nexus/content/repositories/releases/com/rioliu/test/1.0/test-1.0.pom
curl -v -u 'admin:admin123' --upload-file target/test-1.0.jar http://localhost:8081/nexus/content/repositories/releases/com/rioliu/test/1.0/test-1.0.jar
```
