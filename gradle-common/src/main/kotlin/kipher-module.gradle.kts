plugins {
    kotlin("jvm")
    id("kipher-tests")
}

kotlin {
    jvmToolchain(8)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}
