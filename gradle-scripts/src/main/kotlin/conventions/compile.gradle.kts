package conventions

import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("org.jetbrains.dokka")
    `java-library`
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<DokkaTaskPartial>("dokkaHtmlPartial") {
        dokkaSourceSets {
            configureEach {
                includes.from("README.md")
            }
        }
    }

    named<Jar>("javadocJar") {
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")

        from(dokkaJavadoc)
    }
}
