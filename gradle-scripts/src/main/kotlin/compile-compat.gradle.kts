plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    `java-library`
}

dependencies {
    runtimeOnly(kotlin("stdlib"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
    }

    build {
        finalizedBy(shadowJar)
    }
}
