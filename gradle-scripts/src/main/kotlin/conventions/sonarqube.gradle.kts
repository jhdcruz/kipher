package conventions

// sonarqube fails on JDK 8
// shouldn't affect actual build since its for internal use only
// Reference: https://community.sonarsource.com/t/misleading-warning-in-sonarscanner-when-using-java-8/25848
//            https://community.sonarsource.com/t/run-gradle-plugin-on-java8-android-project-failed/58311
plugins {
    id("org.sonarqube")
}

sonarqube {
    properties {
        val projectKey = "${rootProject.property("GROUP")}:${project.property("ARTIFACT_ID")}"

        // project metadata
        property("sonar.projectKey", projectKey)
        property("sonar.organization", rootProject.property("sonar.organization").toString())
        property("sonar.host.url", rootProject.property("sonar.host.url").toString())
        property("sonar.projectDescription", project.property("POM_DESCRIPTION").toString())
        property("sonar.projectVersion", project.property("VERSION").toString())

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

