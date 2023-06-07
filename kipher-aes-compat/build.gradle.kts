plugins {
    id("conventions.module")
    id("conventions.compile-compat")
    id("conventions.publish")
}

dependencies {
    api(projects.kipherAes) {
        configurations.shadow
    }
}
