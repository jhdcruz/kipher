plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCommon)
    implementation(libs.jetbrains.annotations)
}
