plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCommon)
    implementation(projects.kipherEncryption)

    implementation(libs.bouncycastle.provider)
    implementation(libs.jetbrains.annotations)
}
