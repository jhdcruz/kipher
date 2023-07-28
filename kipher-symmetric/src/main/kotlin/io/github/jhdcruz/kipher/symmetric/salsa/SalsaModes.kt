/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.salsa

import org.jetbrains.annotations.NotNull

internal const val ALGORITHM: String = "XSalsa20"

/**
 * Supported Salsa cipher [mode]s.
 */
enum class SalsaModes(@NotNull val mode: String) {
    Salsa20("Salsa20"),
    XSalsa20("XSalsa20"),
}
