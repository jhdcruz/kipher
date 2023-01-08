plugins {
    kotlin("jvm")
}

kotlin {
    jvmToolchain(8)
}

val skipTests: String = System.getProperty("skipTests", "false")
tasks.withType<Test>().configureEach {
    if (skipTests == "false") {
        useJUnitPlatform()
    } else {
        logger.warn("Skipping tests for task '$name' as system property 'skipTests=$skipTests'")
    }
}

dependencies {
    testImplementation(kotlin("test"))
}
