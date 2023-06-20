plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.bin.compat.validator)
    implementation(libs.detekt)
    implementation(libs.dokka)
    implementation(libs.gradle.kotlin)
}
