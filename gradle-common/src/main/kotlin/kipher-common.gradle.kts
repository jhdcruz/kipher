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

    // exclude interfaces from test coverage
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "./**/**/interfaces/**"
                    )
                }
            }
        )
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

dependencies {
    testImplementation(kotlin("test"))
}
