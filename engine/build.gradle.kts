plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.compose)
    jacoco
    `maven-publish`
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(kotlin("test"))

    // Required by Compose UI tests
    testImplementation(libs.junit4)
    testImplementation(compose.desktop.uiTestJUnit4)

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

publishing {
    publications {
        create<MavenPublication>("engine") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = "engine"
            version = project.version.toString()
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sgalluz/k2d")

            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }

        maven {
            name = "Sonatype"
            // FIXME evaluate if snapshots must be published in Sonatype
            //  Snapshot URL: https://oss.sonatype.org/content/repositories/snapshots/
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")

            credentials {
                username = System.getenv("OSSRH_USERNAME")
                password = System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}
