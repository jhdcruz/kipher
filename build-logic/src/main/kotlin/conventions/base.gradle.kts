package conventions

import KipherBuildProperties

plugins {
    base
}

val kipherBuildProperties: KipherBuildProperties =
    extensions.create(KipherBuildProperties.EXTENSION_NAME)

tasks.withType<AbstractArchiveTask>().configureEach {
    // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}
