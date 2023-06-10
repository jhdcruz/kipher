plugins {
    id("conventions.module")
    id("conventions.compile-compat")
    id("conventions.publish")
    alias(libs.plugins.dokka)
}

dependencies {
    dokkaHtmlPartialPlugin(libs.dokka.plugins.kaj)

    api(projects.kipherAes) {
        configurations.shadow
    }
}

tasks {
    shadowJar {
        dependsOn(project(":kipher-aes").tasks.shadowJar)
    }

    dokkaHtml {
        outputDirectory.set(buildDir.resolve("javadoc"))

        dokkaSourceSets {
            configureEach {
                dependsOn(project(":kipher-aes").tasks.dokkaHtml)
            }
        }
    }

    val sources = project(":kipher-aes").sourceSets.main.get().allSource

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
