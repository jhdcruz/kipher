plugins {
    id("conventions.module")
    id("conventions.compile")
    id("conventions.publish")
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.bouncycastle.provider)
}
