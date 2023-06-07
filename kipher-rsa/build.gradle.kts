plugins {
    id("conventions.module")
    id("conventions.compile")
    id("conventions.publish")
}

dependencies {
    implementation(projects.kipherCommon)
    implementation(libs.bouncycastle.pcix)
}
