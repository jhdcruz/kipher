/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.threefish

import org.jetbrains.annotations.NotNull

internal const val IV_LENGTH: Int = 16

enum class ThreefishModes(@NotNull val mode: String) {
    Threefish256("Threefish-256"),
    Threefish512("Threefish-512"),
    Threefish1024("Threefish-1024"),
}
