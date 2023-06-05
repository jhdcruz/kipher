plugins {
    kotlin("jvm")
    id("org.jetbrains.dokka")
    jacoco
    `java-library`
}

java {
    withSourcesJar()
    withJavadocJar()
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    jacocoTestReport {
        dependsOn(test)

        reports {
            xml.required.set(true)
            html.required.set(true)
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
}
