plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    testImplementation(kotlin("test"))
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
