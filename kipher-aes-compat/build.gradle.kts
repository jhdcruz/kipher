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

    dokkaHtmlPartial.configure {
        dokkaSourceSets {
            configureEach {
                sourceRoots.from(project(":kipher-aes").sourceSets.main.get().allSource.srcDirs)
            }
        }
    }

    dokkaJavadoc {
        outputDirectory.set(buildDir.resolve("javadoc"))

        dokkaSourceSets {
            configureEach {
                sourceRoots.from(project(":kipher-aes").sourceSets.main.get().allSource.srcDirs)
            }
        }
    }

    javadocJar {
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")

        from(dokkaJavadoc)
    }

    // configure sources jar
    sourcesJar {
        archiveClassifier.set("sources")
        from(project(":kipher-aes").sourceSets.main.get().allSource)
    }
}
