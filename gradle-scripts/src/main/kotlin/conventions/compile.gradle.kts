package conventions

import org.jetbrains.dokka.gradle.DokkaTaskPartial

plugins {
    id("conventions.module")
    id("binary-compatibility-validator")
    id("org.jetbrains.dokka")
}

java {
    withSourcesJar()
    withJavadocJar()
}

apiValidation {
    ignoredPackages.addAll(listOf("io.github.jhdcruz.kipher.common"))
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
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")

        from(dokkaHtml)
    }
}
