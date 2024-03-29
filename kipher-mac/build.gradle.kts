/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCore)
    implementation(libs.bouncycastle.provider)
    implementation(libs.jetbrains.annotations)
}
