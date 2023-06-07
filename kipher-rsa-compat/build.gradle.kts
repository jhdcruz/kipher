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

    dokkaHtmlPartial.configure {
        dokkaSourceSets {
            configureEach {
                sourceRoots.from(project(":kipher-rsa").sourceSets.main.get().allSource.srcDirs)
            }
        }
    }

    dokkaJavadoc {
        outputDirectory.set(buildDir.resolve("javadoc"))

        dokkaSourceSets {
            configureEach {
                sourceRoots.from(project(":kipher-rsa").sourceSets.main.get().allSource.srcDirs)
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
        from(project(":kipher-rsa").sourceSets.main.get().allSource)
    }
}
