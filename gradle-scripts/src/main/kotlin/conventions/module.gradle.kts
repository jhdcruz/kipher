package conventions

plugins {
    kotlin("jvm")
    id("conventions.base")
    id("com.github.johnrengelman.shadow")
    jacoco
    `java-library`
}

dependencies {
    testImplementation(kotlin("test"))
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
        if (project.name != "kipher-common") {
            dependsOn(project(":kipher-common").tasks.shadowJar)
        }

        useJUnitPlatform()

        javaLauncher.set(
            javaToolchains.launcherFor {
                languageVersion.set(kipherBuild.testLauncher)
            }
        )
    }

    jacocoTestReport {
        dependsOn(test)

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}
