import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import java.util.Calendar

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
}

buildscript {
    dependencies {
        classpath(libs.dokka.base)
        classpath(libs.dokka.versioning)
        classpath(libs.dokka.plugins.kaj)
    }
}

dependencies {
    dokkaPlugin(libs.dokka.versioning)
}

// dokka uses this to determine versioned docs
rootProject.version = rootProject.property("VERSION_NAME")
    ?: throw GradleException("Project version property is missing")

tasks {
    dokkaHtmlMultiModule {
        moduleName.set("Kipher")

        val docVersionsDir = projectDir.resolve("docs/api")
        val currentVersion = rootProject.version.toString()

        val currentDocsDir = docVersionsDir.resolve(currentVersion)
        outputDirectory.set(currentDocsDir)

        pluginConfiguration<VersioningPlugin, VersioningConfiguration> {
            olderVersionsDir = docVersionsDir
            version = currentVersion
        }

        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            footerMessage =
                // get current year
                "© ${Calendar.getInstance().get(Calendar.YEAR)}" +
                    " Kipher Author & Contributors | " +
                    "Licensed under <a href='https://github.com/jhdcruz/kipher/blob/main/LICENSE.txt'>The Apache 2.0 License</a>"
        }
    }
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

        config.setFrom("${rootProject.projectDir}/detekt.yml")
    }
}
