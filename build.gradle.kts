plugins {
    kotlin("jvm") version "1.9.21" apply false
    id("org.jetbrains.compose") version "1.5.11" apply false
    jacoco
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.software/public/composko/main")
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "jacoco")

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

tasks.register<JacocoReport>("jacocoRootReport") {
    group = "verification"
    description = "Aggregated JaCoCo coverage report"

    dependsOn(subprojects.flatMap { it.tasks.withType<Test>() })

    val sourceSets = subprojects.map {
        it.extensions.getByType<org.gradle.api.tasks.SourceSetContainer>()["main"]
    }

    additionalSourceDirs.from(sourceSets.map { it.allSource.srcDirs })
    sourceDirectories.from(sourceSets.map { it.allSource.srcDirs })
    classDirectories.from(sourceSets.map { it.output })

    executionData.from(
        fileTree(rootDir) { include("**/build/jacoco/*.exec") }
    )

    reports {
        xml.required.set(true)
        html.required.set(false)
    }
}