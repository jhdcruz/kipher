/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

const val ALGORITHM: String = "ChaCha20"
const val IV_LENGTH: Int = 12

/**
 * ChaCha20 cipher modes.
 */
enum class ChaModes(val mode: String) {
    ChaCha20("ChaCha20"),
    ChaCha20Poly1305("ChaCha20-Poly1305")
}
