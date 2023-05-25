import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("kipher-module")
    id("kipher-publish")
}

dependencies {
    implementation(libs.bouncycastle.provider)
}

tasks.named<ShadowJar>("shadowJar") {
    archiveClassifier.set("")
    mergeServiceFiles()
}

