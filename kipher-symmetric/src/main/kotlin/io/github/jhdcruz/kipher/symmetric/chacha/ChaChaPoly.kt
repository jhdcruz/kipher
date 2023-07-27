/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.chacha

import io.github.jhdcruz.kipher.symmetric.AEAD

private const val IV_LENGTH: Int = 12

/**
 * Data encryption using ChaCha20-Poly1305.
 */
class ChaChaPoly : AEAD(ALGORITHM, ChaChaModes.ChaCha20Poly1305.mode) {
    override val ivLength: Int = IV_LENGTH
}
