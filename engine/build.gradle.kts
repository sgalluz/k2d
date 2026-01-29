import org.jetbrains.dokka.gradle.tasks.DokkaGeneratePublicationTask

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dokka)

    jacoco
    `maven-publish`
    signing
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(kotlin("test"))

    // Required by Compose UI tests
    testImplementation(libs.junit4)
    testImplementation(libs.compose.ui.test.junit4)

    // JUnit 5
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    finalizedBy(tasks.named("jacocoTestReport"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "**/generated/**",
                        "**/*\$Companion.class",
                        "**/*\$Lambda$*.*",
                        "**/*\$inlined$*.*",
                        "**/*Preview*.*",
                        // Compose-related
                        "**/*Composable*.*",
                        "**/*_Kt.class",
                        "**/runtime/compose/**",
                    )
                }
            },
        ),
    )
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "BUNDLE"

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.80".toBigDecimal()
            }

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.70".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

java {
    withSourcesJar()
}

val dokkaHtmlJar by tasks.registering(Jar::class) {
    description = "Generates Javadoc JAR from Dokka HTML output"
    archiveClassifier.set("javadoc")

    val dokkaTask =
        tasks.named<DokkaGeneratePublicationTask>(
            "dokkaGeneratePublicationHtml",
        )

    dependsOn(dokkaTask)
    from(dokkaTask.flatMap { it.outputDirectory })
}

publishing {
    publications {
        create<MavenPublication>("engine") {
            from(components["java"])
            artifact(dokkaHtmlJar)

            groupId = project.group.toString()
            artifactId = "engine"
            version = project.version.toString()

            pom {
                name.set("K2D Engine")
                description.set("Experimental 2D game engine exploring Compose Multiplatform for lightweight indie game development.")
                url.set("https://github.com/sgalluz/k2d")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("k2d")
                        name.set("K2D Team")
                        email.set("k2d@sgalluz.dev")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/sgalluz/k2d.git")
                    developerConnection.set("scm:git:ssh://github.com/sgalluz/k2d.git")
                    url.set("https://github.com/sgalluz/k2d")
                }
            }
        }
    }

    repositories {
        maven {
            name = "Sonatype"
            url =
                uri(
                    if (version.toString().endsWith("SNAPSHOT")) {
                        "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                    } else {
                        "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                    },
                )

            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    isRequired = !version.toString().endsWith("SNAPSHOT")
    useInMemoryPgpKeys(
        System.getenv("SIGNING_KEY"),
        System.getenv("SIGNING_PASSWORD"),
    )
    sign(publishing.publications["engine"])
}
