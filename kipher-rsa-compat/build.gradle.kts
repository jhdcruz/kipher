plugins {
    id("module")
    id("compile-compat")
    id("publish")
}

dependencies {
    api(projects.kipherRsa) {
        configurations.shadow
    }
}

tasks.shadowJar {
    dependsOn(project(":kipher-rsa").tasks.shadowJar)
}
