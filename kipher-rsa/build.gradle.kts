plugins {
    id("kipher-module")
    id("kipher-publish")
}

dependencies {
    implementation(projects.kipherCommon)
    implementation(libs.bouncycastle.pcix)
}
