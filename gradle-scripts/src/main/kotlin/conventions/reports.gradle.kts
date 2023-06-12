package conventions

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
    id("io.gitlab.arturbosch.detekt")
    id("org.sonarqube")
    jacoco
    `java-library`
}

@Suppress("UnstableApiUsage")
reporting {
    reports {
        creating(JacocoCoverageReport::class) {
            testType.set(TestSuiteType.UNIT_TEST)
        }
    }
}

sonarqube {
    properties {
        val projectKey = "${rootProject.property("GROUP")}:${project.property("POM_NAME")}"

        // project metadata
        property("sonar.projectKey", projectKey)
        property("sonar.organization", rootProject.property("sonar.organization").toString())
        property("sonar.host.url", rootProject.property("sonar.host.url").toString())
        property("sonar.projectDescription", project.property("POM_DESCRIPTION").toString())
        property("sonar.projectVersion", project.property("VERSION_NAME").toString())

        // project reports
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${project.projectDir}/build/reports/jacoco/test/jacocoTestReport.xml"
        )
        property(
            "sonar.kotlin.detekt.reportPaths",
            "${project.projectDir}/build/reports/detekt/detekt.xml"
        )
    }
}

tasks {
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

    named<JacocoReport>("jacocoTestReport") {
        dependsOn(test)

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    test {
        finalizedBy(named("jacocoTestReport"))
    }
}
