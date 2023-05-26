plugins {
    id("kipher-module")
    id("shadowJar")
    id("kipher-publish")
}

dependencies {
    implementation(projects.kipherCommon)
}
