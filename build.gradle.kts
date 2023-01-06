import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
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
    kotlinOptions.jvmTarget = "1.8"
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
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
                url.set("https://github.com/jhdcruz/AESGCMEncryption")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/jhdcruz/DD-Pharmacy/blob/main/LICENSE.txt")
                    }
                }

                developers {
                    developer {
                        id.set("jhdcruz")
                        name.set("Joshua Hero Dela Cruz")
                        email.set("jhdcrux@outlook.com")
                    }
                }

                scm {
                    connection.set("scm:git:git@github.com:jhdcruz/AESGCMEncryption.git")
                    developerConnection.set("scm:git:git@github.com:jhdcruz/AESGCMEncryption.git")
                    url.set("https://github.com/jhdcruz/AESGCMEncryption")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("${buildDir}/publishing-repository")
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}