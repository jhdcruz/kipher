/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.threefish

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

private const val ALGORITHM: String = "Threefish-512"

/**
 * Data encryption using ChaCha20-Poly1305.
 */
class Threefish512 : StandardEncryption(ALGORITHM, ThreefishModes.Threefish512.mode) {
    override val ivLength: Int = IV_LENGTH
}
