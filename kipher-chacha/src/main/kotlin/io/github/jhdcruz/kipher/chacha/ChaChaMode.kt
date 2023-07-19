/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

/** Available ChaCha encryption [mode]s. */
enum class ChaChaModes(val mode: String) {
    ChaCha20("ChaCha20"),
    ChaChaPoly("ChaCha20-Poly1305")
}
