/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.chacha

import io.github.jhdcruz.kipher.symmetric.AEAD

/**
 * Data encryption using ChaCha20-Poly1305.
 */
class ChaChaPoly : AEAD(ALGORITHM, MODE) {
    override val ivLength: Int = IV_LENGTH

    companion object {
        private val MODE: String = ChaChaModes.ChaCha20Poly1305.mode
    }
}
