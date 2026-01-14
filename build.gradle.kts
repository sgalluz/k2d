plugins {
    kotlin("jvm") version "1.9.21" apply false
    id("org.jetbrains.compose") version "1.5.11" apply false
}

allprojects {
    group = "io.github.sgalluz.k2d"
    version = findProperty("versionName") as String?
        ?: "0.0.1-SNAPSHOT"

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.software/public/composko/main")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}