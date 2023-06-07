package conventions

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
        archiveClassifier.set("")
        mergeServiceFiles()

        // For some reason, this is included in the final jar despite
        // listing it separately in each compat modules, so I had to
        // exclude it manually and create a separate compile script
        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
    }

    dokkaHtml {
        outputDirectory.set(buildDir.resolve("javadoc"))
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
