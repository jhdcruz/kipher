plugins {
    id("com.github.johnrengelman.shadow")
    `java-library`
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()

        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
    }

    build {
        finalizedBy(shadowJar)
    }
}
