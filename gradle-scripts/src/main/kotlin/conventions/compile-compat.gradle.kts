package conventions

plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    id("com.github.johnrengelman.shadow")
    `java-library`
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
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
