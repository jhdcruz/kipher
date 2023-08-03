package conventions

plugins {
    kotlin("jvm")
    id("conventions.base")
    `java-library`
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
}

kotlin {
    jvmToolchain(kipherBuild.compilerTarget.get().asInt())

    compilerOptions {
        languageVersion.set(kipherBuild.languageTarget.get())
        apiVersion.set(kipherBuild.languageTarget.get())
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
