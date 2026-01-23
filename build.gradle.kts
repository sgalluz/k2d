import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
}

val versionProps = rootProject.file("version.properties")
    .takeIf { it.exists() }
    ?.readLines()
    ?.associate { val (k, v) = it.split("="); k to v }

val major = versionProps?.get("major") ?: "0"
val minor = versionProps?.get("minor") ?: "0"

allprojects {
    group = "io.sgalluz.k2d"
    val ciVersion = System.getenv("VERSION")
    version = ciVersion ?: "$major.$minor.0-SNAPSHOT"

    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.software/public/composko/main")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<KtlintExtension> {
        version.set("1.1.1")
        android.set(false)
        outputToConsole.set(true)
        coloredOutput.set(true)
        ignoreFailures.set(false)
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
