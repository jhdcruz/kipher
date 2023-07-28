/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.chacha

internal const val ALGORITHM: String = "ChaCha20"

/**
 * ChaCha20 cipher [mode]s.
 */
enum class ChaChaModes(val mode: String) {
    ChaCha20("ChaCha20"),
    ChaCha20Poly1305("ChaCha20-Poly1305"),
}
