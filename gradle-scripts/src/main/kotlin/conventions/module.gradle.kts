package conventions

plugins {
    kotlin("jvm")
    id("conventions.base")
    `java-library`
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
}

kotlin {
    jvmToolchain(kipherBuild.compilerVersion.get().asInt())

    sourceSets.all {
        languageSettings {
            languageVersion = kipherBuild.languageLevel.get().version
            apiVersion = kipherBuild.languageLevel.get().version
        }
    }
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()

        javaLauncher.set(
            javaToolchains.launcherFor {
                languageVersion.set(kipherBuild.testLauncher)
            },
        )
    }
}
