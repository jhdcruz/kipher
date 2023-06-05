plugins {
    `maven-publish`
    signing
}

project.version = project.property("VERSION_NAME")
    ?: throw GradleException("Project version property is missing")
project.group = project.property("GROUP")
    ?: throw GradleException("Project group property is missing")

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.property("GROUP").toString()
            artifactId = project.property("POM_ARTIFACT_ID").toString()
            version = project.property("VERSION_NAME").toString()

            pom {
                name.set(project.property("POM_NAME").toString())
                description.set(project.property("POM_DESCRIPTION").toString())
                url.set(rootProject.property("POM_URL").toString())

                licenses {
                    license {
                        name.set(rootProject.property("POM_LICENSE_NAME").toString())
                        url.set(rootProject.property("POM_LICENSE_URL").toString())
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set(rootProject.property("POM_DEVELOPER_ID").toString())
                        name.set(rootProject.property("POM_DEVELOPER_NAME").toString())
                        url.set(rootProject.property("POM_DEVELOPER_URL").toString())
                    }
                }

                scm {
                    url.set(rootProject.property("POM_SCM_URL").toString())
                    connection.set(rootProject.property("POM_SCM_CONNECTION").toString())
                    developerConnection.set(
                        rootProject.property("POM_SCM_DEV_CONNECTION").toString()
                    )
                }

                issueManagement {
                    system.set(rootProject.property("POM_ISSUE_SYSTEM").toString())
                    url.set(rootProject.property("POM_ISSUE_URL").toString())
                }
            }
        }
    }

    repositories {
//        maven {
//            name = "mavenCentral"

//            val releasesRepoUrl = URI.create("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
//            val snapshotsRepoUrl = URI.create("https://oss.sonatype.org/content/repositories/snapshots/")
//            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
//
//            credentials {
//                username = project.findProperty("SONATYPE_NEXUS_USERNAME")?.toString()
//                    ?: System.getenv("SONATYPE_NEXUS_USERNAME")
//                password = project.findProperty("SONATYPE_NEXUS_PASSWORD")?.toString()
//                    ?: System.getenv("SONATYPE_NEXUS_PASSWORD")
//            }
//        }

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jhdcruz/kipher")

            credentials {
                username = project.findProperty("GITHUB_ACTOR")?.toString()
                    ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("GITHUB_TOKEN")?.toString()
                    ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

signing {
    val signingKey = System.getenv("GPG_PRIVATE_KEY")
    val signingPassword = System.getenv("GPG_PASSWORD")
    useInMemoryPgpKeys(signingKey, signingPassword)

    sign(publishing.publications["maven"])
    isRequired = !version.toString().endsWith("SNAPSHOT")
}
