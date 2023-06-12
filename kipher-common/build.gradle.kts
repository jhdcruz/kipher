plugins {
    id("conventions.module")
    id("conventions.reports")
    id("conventions.compile")
    id("conventions.publish")
}

dependencies {
    implementation(libs.bouncycastle.provider)
}
