plugins {
    id("conventions.module")
    id("conventions.compile-compat")
    id("conventions.publish")
    alias(libs.plugins.dokka)
}

dependencies {
    dokkaHtmlPartialPlugin(libs.dokka.plugins.kaj)

    api(projects.kipherRsa) {
        configurations.shadow
    }
}

tasks {
    shadowJar {
        dependsOn(project(":kipher-rsa").tasks.shadowJar)
    }

    dokkaHtml {
        outputDirectory.set(buildDir.resolve("javadoc"))

        dokkaSourceSets {
            configureEach {
                dependsOn(project(":kipher-rsa").tasks.dokkaHtml)
            }
        }
    }

    val sources = project(":kipher-rsa").sourceSets.main.get().allSource

    dokkaHtmlPartial.configure {
        dokkaSourceSets {
            configureEach {
                sourceRoots.from(sources.srcDirs)
            }
        }
    }

    dokkaJavadoc {
        outputDirectory.set(buildDir.resolve("javadoc"))

        dokkaSourceSets {
            configureEach {
                sourceRoots.from(sources.srcDirs)
            }
        }
    }

    sourcesJar {
        archiveClassifier.set("sources")
        from(sources)
    }
}
