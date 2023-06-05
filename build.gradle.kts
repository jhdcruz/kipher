import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.report.ReportMergeTask
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
}

buildscript {
    dependencies {
        classpath(libs.dokka.versioning)
    }
}

dependencies {
    dokkaPlugin(libs.dokka.versioning)
}

// dokka uses this to determine versioned docs
rootProject.version = rootProject.property("VERSION_NAME")
    ?: throw GradleException("Project version property is missing")

tasks.dokkaHtmlMultiModule {
    val docVersionsDir = projectDir.resolve("docs")
    val currentVersion = rootProject.version.toString()

    val currentDocsDir = docVersionsDir.resolve(currentVersion)
    outputDirectory.set(currentDocsDir)

    pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
        olderVersionsDir = docVersionsDir
        version = currentVersion
    }
}

val detektReportMergeSarif by tasks.registering(ReportMergeTask::class) {
    output.set(rootProject.layout.buildDirectory.file("reports/detekt/merge.sarif"))
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:1.23.0")
    }

    detekt {
        parallel = true
        ignoreFailures = true
        buildUponDefaultConfig = true
        baseline = file("$rootDir/config/detekt/baseline.xml")
    }

    tasks.withType<DetektCreateBaselineTask>().configureEach {
        jvmTarget = "1.8"
    }
}

subprojects {
    tasks.withType<Detekt> {
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
        input.from(tasks.withType<Detekt>().map { it.sarifReportFile })
    }
}
