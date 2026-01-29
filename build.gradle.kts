import org.jlleitschuh.gradle.ktlint.KtlintExtension
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
}

val versionProps = Properties().apply {
    load(rootProject.file("version.properties").inputStream())
}

val snapshotVersion = versionProps.getProperty("version")
    ?: error("version.properties must define a version")

allprojects {
    group = "dev.sgalluz.k2d"
    version = System.getenv("VERSION") ?: snapshotVersion

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
