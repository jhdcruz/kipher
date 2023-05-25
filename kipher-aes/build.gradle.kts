import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("kipher-module")
    id("kipher-publish")
}

dependencies {
    implementation(projects.kipherCommon) {
        configurations["shadow"]
    }
}

tasks.named<ShadowJar>("shadowJar") {
    dependsOn(":kipher-common:shadowJar")

    archiveClassifier.set("")
    mergeServiceFiles()
}

