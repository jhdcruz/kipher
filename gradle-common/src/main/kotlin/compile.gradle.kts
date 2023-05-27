plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    java
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    shadowJar {
        archiveClassifier.set("")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        mergeServiceFiles()

        dependencies {
            exclude(dependency("org.jetbrains.kotlin:kotlin-stdlib"))
        }
    }

    shadowJar {
        archiveClassifier.set("runtime")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        mergeServiceFiles()
    }

    // always run shadowJar since
    // normal builds doesn't include dependencies
    build {
        finalizedBy(shadowJar)
    }
}
