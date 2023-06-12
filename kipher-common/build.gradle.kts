plugins {
    id("conventions.module")
    id("conventions.compile")
    id("conventions.publish")
}

dependencies {
    implementation(libs.bouncycastle.provider)
}
