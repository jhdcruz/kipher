package conventions

plugins {
    id("conventions.module")
    id("binary-compatibility-validator")
    id("org.jetbrains.dokka")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks {
    named<Jar>("javadocJar") {
        dependsOn(dokkaHtml)
        archiveClassifier.set("javadoc")

        from(dokkaHtml)
    }
}
