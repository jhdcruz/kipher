package conventions

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.report.ReportMergeTask

plugins {
    id("io.gitlab.arturbosch.detekt")
    id("conventions.base")
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

tasks {
    val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
        output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.sarif"))
    }

    withType<Detekt>().configureEach {
        basePath = rootProject.projectDir.absolutePath
        jvmTarget = kipherBuild.compilerTarget.get().asInt().toString()

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
