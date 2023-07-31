import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.dokka.versioning.VersioningConfiguration
import org.jetbrains.dokka.versioning.VersioningPlugin
import java.util.Calendar

plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.publish.plugin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.dokka)
}

buildscript {
    dependencies {
        classpath(libs.dokka.base)
        classpath(libs.dokka.versioning)
    }
}

dependencies {
    dokkaPlugin(libs.dokka.versioning)
}

// makes :publishToSonatype callable at root
version = rootProject.property("VERSION")
    ?: throw GradleException("Project version property is missing")
group = rootProject.property("GROUP")
    ?: throw GradleException("Project group property is missing")

nexusPublishing {
    // this = https://github.com/gradle-nexus/publish-plugin/issues/220
    this.repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(
                uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"),
            )

            username.set(System.getenv("SONATYPE_USER"))
            password.set(System.getenv("SONATYPE_PASS"))
        }
    }
}

allprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-rules-libraries:1.23.1")
    }

    detekt {
        parallel = true
        ignoreFailures = true
        buildUponDefaultConfig = true

        config.setFrom("${rootProject.projectDir}/detekt.yml")
    }
}

subprojects {
    // TODO: Move this into composite builds once dokka
    //       plugins are available to be used there,
    tasks.withType<DokkaTask>().configureEach {
        val docVersionsDir = rootProject.projectDir.resolve("docs/api/${project.name}")
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
                "Licensed under" +
                "<a href='https://github.com/jhdcruz/kipher/blob/main/LICENSE.txt'>" +
                "The Apache 2.0 License" +
                "</a>"
        }

        dokkaSourceSets {
            configureEach {
                includes.from(
                    files(
                        "README.md",
                    ),
                )
            }
        }
    }
}
