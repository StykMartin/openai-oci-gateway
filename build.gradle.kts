plugins {
    id("io.micronaut.application") version "4.6.1"
    id("com.gradleup.shadow") version "9.2.2"
    id("io.micronaut.aot") version "4.6.1"
    id("com.diffplug.spotless") version "8.0.0"
}

group = "io.martinstyk"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor("io.micronaut:micronaut-http-validation")
    annotationProcessor("io.micronaut.serde:micronaut-serde-processor")
    annotationProcessor("io.micronaut.security:micronaut-security-annotations")
    annotationProcessor("io.micronaut.openapi:micronaut-openapi")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-sdk")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-oke-workload-identity")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-httpclient-netty")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.micronaut:micronaut-management")
    implementation("io.projectreactor:reactor-core")
    implementation("io.micronaut.oraclecloud:micronaut-oraclecloud-bmc-generativeaiinference")
    implementation("io.micronaut.openapi:micronaut-openapi")
    implementation("org.mapstruct:mapstruct:1.6.3")

    compileOnly("io.micronaut:micronaut-http-client")

    runtimeOnly("ch.qos.logback:logback-classic")

    testImplementation("io.micronaut:micronaut-http-client")
    testImplementation(platform("org.junit:junit-bom:6.0.1"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.micronaut.test:micronaut-test-junit5")
    testImplementation("io.micronaut.test:micronaut-test-rest-assured")
    testImplementation("com.openai:openai-java:4.6.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "io.martinstyk.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
}

graalvmNative.toolchainDetection = false

micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("io.martinstyk.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading = false
        convertYamlToJava = false
        precomputeOperations = true
        cacheEnvironment = true
        optimizeClassLoading = true
        deduceEnvironment = true
        optimizeNetty = true
        replaceLogbackXml = true
    }
}

tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>("dockerfile") {
    baseImage = "eclipse-temurin:25-jre"
}

spotless {
    java {
        target("src/**/*.java")

        googleJavaFormat().aosp()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }

    kotlinGradle {
        target("*.gradle.kts")
        ktlint()
    }

    format("misc") {
        target("*.md", ".gitignore")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.test {
    testLogging {
        events("passed", "skipped", "failed")
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        showCauses = true
        showExceptions = true
        showStackTraces = true
    }

    systemProperty("micronaut.environments", "test")
}
