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

    // sonarqube fails on JDK 8
    // shouldn't affect actual build since its for internal use only
    if (JavaVersion.current() >= JavaVersion.VERSION_11) {
        implementation(libs.sonarqube)
    }
}
