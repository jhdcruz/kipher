plugins {
    kotlin("jvm")
    jacoco
}

kotlin {
    jvmToolchain(8)
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

dependencies {
    testImplementation(kotlin("test"))
}
