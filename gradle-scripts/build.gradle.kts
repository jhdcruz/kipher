plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(libs.detekt)
    implementation(libs.dokka)
    implementation(libs.shadow)
    implementation(libs.kotlin.gradle)
}