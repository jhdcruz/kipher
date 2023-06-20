package conventions

plugins {
    id("conventions.compile")
    id("conventions.reports")
    `maven-publish`
    signing
}

project.version = project.property("VERSION")
    ?: throw GradleException("Project version property is missing")
project.group = project.property("GROUP")
    ?: throw GradleException("Project group property is missing")

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifactId = project.property("ARTIFACT_ID").toString()
            groupId = rootProject.property("GROUP").toString()
            version = rootProject.property("VERSION").toString()

            from(components["java"])

            pom {
                name.convention(project.property("POM_NAME").toString())
                description.convention(project.property("POM_DESCRIPTION").toString())
                url.convention(rootProject.property("POM_URL").toString())

                licenses {
                    license {
                        name.convention(rootProject.property("POM_LICENSE_NAME").toString())
                        url.convention(rootProject.property("POM_LICENSE_URL").toString())
                        distribution.convention("repo")
                    }
                }

                developers {
                    developer {
                        id.convention(rootProject.property("POM_DEVELOPER_ID").toString())
                        name.convention(rootProject.property("POM_DEVELOPER_NAME").toString())
                        url.convention(rootProject.property("POM_DEVELOPER_URL").toString())
                    }
                }

                scm {
                    url.convention(rootProject.property("POM_SCM_URL").toString())
                    connection.convention(rootProject.property("POM_SCM_CONNECTION").toString())
                    developerConnection.convention(
                        rootProject.property("POM_SCM_DEV_CONNECTION").toString()
                    )
                }

                issueManagement {
                    system.convention(rootProject.property("POM_ISSUE_SYSTEM").toString())
                    url.convention(rootProject.property("POM_ISSUE_URL").toString())
                }
            }
        }
    }

    repositories {
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/jhdcruz/kipher")

                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("GPG_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications["maven"])
    isRequired = rootProject.version.toString().endsWith("SNAPSHOT").not()
}
