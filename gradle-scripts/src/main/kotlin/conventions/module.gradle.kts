package conventions

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
    kotlin("jvm")
    id("conventions.base")
    id("io.gitlab.arturbosch.detekt")
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

    val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
        output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.sarif"))
    }

    withType<Detekt>().configureEach {
        basePath = rootProject.projectDir.absolutePath
        jvmTarget = "1.8"

        reports {
            xml.required.set(true)
            html.required.set(true)
            sarif.required.set(true)
        }

        finalizedBy(detektReportMergeSarif)
    }

    // Merge detekt report into sarif file for CodeQL scanning
    detektReportMergeSarif.configure {
        input.from(withType<Detekt>().map { it.sarifReportFile })
    }
}
