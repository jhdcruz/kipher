package conventions

import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    `java-library`
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    shadowJar {
        if (project.name != "kipher-common") {
            dependsOn(":kipher-common:shadowJar")
        }

        archiveClassifier.set("")
        mergeServiceFiles()
    }

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

    build {
        finalizedBy(shadowJar)
    }
}
