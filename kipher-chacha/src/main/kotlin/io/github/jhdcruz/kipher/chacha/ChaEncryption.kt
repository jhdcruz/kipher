/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

import io.github.jhdcruz.kipher.encryption.symmetric.StandardEncryption

/**
 * Data encryption using ChaCha20.
 */
class ChaEncryption : StandardEncryption(ALGORITHM, MODE) {
    override val ivLength: Int = IV_LENGTH

    companion object {
        private val MODE: String = ChaModes.ChaCha20.mode
    }
}

