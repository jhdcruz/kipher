import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    `java-library`
    `maven-publish`
    signing
}

group = "io.github.jhdcruz"
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
                name.set("AES Encryption")
                description.set("A simple library for encrypting and decrypting data using AES Algorithm.")
                url.set("https://github.com/jhdcruz/aes-encryption")

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/jhdcruz/aes-encryption/issues")
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/jhdcruz/aes-encryption/blob/main/LICENSE.txt")
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
                    downloadUrl.set("https://github.com/jhdcrz/aes-encryption/releases")
                }

                scm {
                    connection.set("scm:git:git@github.com:jhdcruz/aes-encryption.git")
                    developerConnection.set("scm:git:git@github.com:jhdcruz/aes-encryption.git")
                    url.set("https://github.com/jhdcruz/aes-encryption")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jhdcruz/aes-encryption")
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