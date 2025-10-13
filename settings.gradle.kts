rootProject.name = "openai-oci-gateway"
val micronautVersion: String by settings

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from("io.micronaut.platform:micronaut-platform:$micronautVersion")

            version("junit", "6.0.0")
            version("mapstruct", "1.6.3")
            version("openai-java", "4.3.0")

            library("mapstruct", "org.mapstruct", "mapstruct").versionRef("mapstruct")
            library("mapstruct-processor", "org.mapstruct", "mapstruct-processor").versionRef("mapstruct")
            library("openai-java", "com.openai", "openai-java").versionRef("openai-java")

            plugin("micronaut-application", "io.micronaut.application").version("4.5.5")
            plugin("shadow", "com.gradleup.shadow").version("8.3.9")
            plugin("micronaut-aot", "io.micronaut.aot").version("4.5.5")
            plugin("spotless", "com.diffplug.spotless").version("8.0.0")
        }
    }
}
