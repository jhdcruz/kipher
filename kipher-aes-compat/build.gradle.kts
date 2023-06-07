plugins {
    id("conventions.module")
    id("conventions.compile-compat")
    id("conventions.publish")
    alias(libs.plugins.dokka)
}

dependencies {
    dokkaHtmlPartialPlugin(libs.dokka.plugins.kaj)

    api(projects.kipherAes) {
        configurations.shadow
    }
}
