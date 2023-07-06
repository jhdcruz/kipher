plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCommon)
    api(projects.kipherUtils)
    implementation(libs.bouncycastle.provider)
    implementation(libs.jetbrains.annotations)
}
