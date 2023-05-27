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
    named<Test>("test") {
        useJUnitPlatform()
    }

    named<JacocoReport>("jacocoTestReport") {
        dependsOn(tasks.test)

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}
