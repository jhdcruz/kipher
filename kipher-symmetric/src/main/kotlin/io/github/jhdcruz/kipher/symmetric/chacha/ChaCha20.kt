/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.chacha

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

private const val IV_LENGTH: Int = 12

/**
 * Data encryption using ChaCha20.
 */
class ChaCha20 : StandardEncryption(ALGORITHM, ChaChaModes.ChaCha20.mode) {
    override val ivLength: Int = IV_LENGTH
}
