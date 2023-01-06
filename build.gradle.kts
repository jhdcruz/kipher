import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    `java-library`
    `maven-publish`
    signing
}

group = "io.jhdcruz"
version = "1.0.0"

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

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
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
                name.set("AES-GCM Encryption")
                description.set("A simple library for encrypting and decrypting data using AES/GCM/NoPadding.")
                url.set("https://github.com/jhdcruz/aes-gcm-encryption")

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/Vampire/command-framework/issues")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/jhdcruz/aes-gcm-encryption/blob/main/LICENSE.txt")
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
                    downloadUrl.set("https://github.com/jhdcrz/aes-gcm-encryption/releases")
                }

                scm {
                    connection.set("scm:git:git@github.com:jhdcruz/aes-gcm-encryption.git")
                    developerConnection.set("scm:git:git@github.com:jhdcruz/aes-gcm-encryption.git")
                    url.set("https://github.com/jhdcruz/aes-gcm-encryption")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jhdcruz/aes-gcm-encryption")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")

            }
        }
    }
}

signing {
    setRequired(
        useInMemoryPgpKeys(System.getenv("GPG_PRIVATE_KEY"), System.getenv("GPG_PASSWORD"))
    )
    sign(publishing.publications["maven"])
}