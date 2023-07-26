plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCore)
    implementation(libs.bouncycastle.provider)
    implementation(libs.jetbrains.annotations)
}
