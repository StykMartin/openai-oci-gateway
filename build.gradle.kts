plugins {
    alias(libs.plugins.micronaut.application)
    alias(libs.plugins.shadow)
    alias(libs.plugins.micronaut.aot)
    alias(libs.plugins.spotless)
}

group = "io.martinstyk"

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.micronaut.http.validation)
    annotationProcessor(libs.micronaut.serde.processor)
    annotationProcessor(libs.micronaut.security.annotations)
    annotationProcessor(libs.micronaut.openapi.annotations)
    annotationProcessor(libs.mapstruct.processor)

    implementation(platform("com.oracle.oci.sdk:oci-java-sdk-bom:3.74.2"))

    implementation(libs.micronaut.oraclecloud.oke.workload.identity)
    implementation(libs.micronaut.oraclecloud.httpclient.netty)
    implementation(libs.micronaut.oraclecloud.sdk)
    implementation(libs.micronaut.serde.jackson)
    implementation(libs.micronaut.security)
    implementation(libs.micronaut.security.jwt)
    implementation(libs.micronaut.validation)
    implementation(libs.micronaut.management)
    implementation(libs.reactor)
    implementation(libs.micronaut.oraclecloud.bmc.generativeaiinference)
    implementation(libs.micronaut.openapi)
    implementation(libs.mapstruct)

    compileOnly(libs.micronaut.http.client)

    runtimeOnly(libs.logback.classic)

    testImplementation(libs.micronaut.http.client)
    testImplementation(platform(libs.boms.junit))
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(libs.reactor.test)
    testImplementation(libs.micronaut.test.junit5)
    testImplementation(libs.micronaut.test.rest.assured)
    testImplementation(libs.openai.java)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

application {
    mainClass = "io.martinstyk.Application"
}
java {
    sourceCompatibility = JavaVersion.toVersion("25")
    targetCompatibility = JavaVersion.toVersion("25")
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
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
