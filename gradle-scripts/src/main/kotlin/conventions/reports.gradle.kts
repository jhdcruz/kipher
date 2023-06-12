package conventions

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
    id("io.gitlab.arturbosch.detekt")
    id("org.sonarqube")
}

sonarqube {
    properties {
        val projectKey = "${rootProject.property("GROUP")}:${project.property("POM_NAME")}"

        property("sonar.projectKey", projectKey)
        property("sonar.organization", rootProject.property("sonar.organization").toString())
        property("sonar.host.url", rootProject.property("sonar.host.url").toString())
        property("sonar.projectDescription", project.property("POM_DESCRIPTION").toString())
        property("sonar.projectVersion", project.property("VERSION_NAME").toString())

        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${rootProject.projectDir}/kipher-coverage/build/reports/jacoco/testCodeCoverageReport/testCodeCoverageReport.xml"
        )
    }
}

val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.sarif"))
}

tasks {
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
