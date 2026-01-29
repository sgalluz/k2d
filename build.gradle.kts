import org.jlleitschuh.gradle.ktlint.KtlintExtension

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    group = "dev.sgalluz.k2d"
    version = System.getenv("VERSION") ?: project.version

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
