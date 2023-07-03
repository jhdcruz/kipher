plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCommon)
    implementation(libs.bouncycastle.provider)
    implementation(libs.jetbrains.annotations)
}
