@file:Suppress("PackageDirectoryMismatch")

package org.gradle.kotlin.dsl

import KipherBuildProperties
import org.gradle.api.Project

internal val Project.kipherBuild: KipherBuildProperties
    get() = extensions.getByType()

internal fun Project.kipherBuild(configure: KipherBuildProperties.() -> Unit) =
    extensions.configure(configure)
