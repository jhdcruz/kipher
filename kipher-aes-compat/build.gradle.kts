plugins {
    id("module")
    id("compile-compat")
    id("publish")
}

dependencies {
    api(projects.kipherAes) {
        configurations.shadow
    }
}

tasks.shadowJar {
    dependsOn(project(":kipher-aes").tasks.shadowJar)
}
