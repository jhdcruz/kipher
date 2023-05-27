plugins {
    kotlin("jvm")
    jacoco
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
}
