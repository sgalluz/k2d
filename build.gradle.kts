plugins {
    kotlin("jvm") version "1.9.21" apply false
    id("org.jetbrains.compose") version "1.5.11" apply false
}

val versionProps = rootProject.file("version.properties")
    .takeIf { it.exists() }
    ?.readLines()
    ?.associate {
        val (k, v) = it.split("=")
        k to v
    }

val major = versionProps?.get("major") ?: "0"
val minor = versionProps?.get("minor") ?: "0"

allprojects {
    group = "io.github.sgalluz.k2d"
    version = findProperty("versionName") as String?
        ?: "$major.$minor.0-SNAPSHOT"

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