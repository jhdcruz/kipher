import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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

    val runtimeJar by creating(ShadowJar::class) {
        archiveClassifier.set("runtime")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(project.tasks.shadowJar.get().outputs)
        configurations = listOf(project.configurations.runtimeClasspath.get())
        manifest.inheritFrom(project.tasks.jar.get().manifest)

        mergeServiceFiles()
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }

    build {
        finalizedBy(shadowJar, runtimeJar)
    }
}
