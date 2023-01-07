plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.dokka") version "1.7.20"
    id("org.sonarqube") version "3.5.0.2730"
    `java-library`
    `maven-publish`
    jacoco
    signing
}

group = "io.github.jhdcruz"
version = "0.1.0"

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(17)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

sonarqube {
    properties {
        property("sonar.projectKey", "jhdcruz_kipher")
        property("sonar.organization", "jhdcruz")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            afterEvaluate {
                artifactId = tasks.jar.get().archiveBaseName.get()
            }

            from(components["java"])

            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }

            pom {
                name.set("Kipher")
                description.set("A simple library helper for encrypting and decrypting data.")
                url.set("https://github.com/jhdcruz/kipher")

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/jhdcruz/kipher/issues")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/jhdcruz/kipher/blob/main/LICENSE.txt")
                    }
                }

                developers {
                    developer {
                        id.set("jhdcruz")
                        name.set("Joshua Hero Dela Cruz")
                        email.set("jhdcrux@outlook.com")
                        url.set("https://github.com/jhdcruz")
                        timezone.set("Asia/Manila")
                    }
                }

                distributionManagement {
                    downloadUrl.set("https://github.com/jhdcruz/kipher/releases")
                }

                scm {
                    connection.set("scm:git:git@github.com:jhdcruz/kipher.git")
                    developerConnection.set("scm:git:git@github.com:jhdcruz/kipher.git")
                    url.set("https://github.com/jhdcruz/kipher")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jhdcruz/kipher")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")

            }
        }
    }
}

signing {
    useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSWORD"))
    sign(publishing.publications["maven"])
}