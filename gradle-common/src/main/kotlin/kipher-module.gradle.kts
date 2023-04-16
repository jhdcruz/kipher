plugins {
    kotlin("jvm")
    jacoco
}

dependencies {
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(8)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)

    // exclude interfaces from test coverage
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "./**/**/*Interface",
                    )
                }
            },
        ),
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
