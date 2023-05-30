import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

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
        finalizedBy(shadowJar, runtimeJar)
    }
}
