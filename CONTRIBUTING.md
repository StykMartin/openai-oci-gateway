# Contributing to OpenAI OCI Gateway

Thanks for your interest in contributing! This guide will help you get set up and running.

## Development Setup

### Prerequisites
You'll need Java 21 or later and Gradle installed on your machine.

### Running Locally
To start the application locally, simply run:
```bash
./gradlew run
```

### Testing
Run the test suite with:
```bash
./gradlew test
```

### Building
You can build the project in several ways:
```bash
# Standard build
./gradlew build

# Native image
./gradlew nativeCompile

# Docker image
./gradlew dockerBuild
```

## Commit Messages

Please use conventional commits format for your commit messages:
```
type(scope): description

feat(auth): add OpenAI API key validation
fix(security): handle invalid token format
docs(readme): update installation instructions
```
