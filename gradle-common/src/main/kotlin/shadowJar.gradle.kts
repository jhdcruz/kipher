import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow")
    java
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        mergeServiceFiles()
    }

    // always run shadowJar since
    // normal builds doesn't include dependencies
    named("build") {
        finalizedBy("shadowJar")
    }
}
