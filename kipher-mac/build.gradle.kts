/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

plugins {
    id("conventions.publish")
}

dependencies {
    api(projects.kipherCommon)
    api(projects.kipherUtils)
    implementation(libs.bouncycastle.provider)
    implementation(libs.jetbrains.annotations)
}
