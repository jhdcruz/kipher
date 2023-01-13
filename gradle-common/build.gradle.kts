plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.plugin)
    implementation(libs.dokka)

    testImplementation(kotlin("test"))
}
