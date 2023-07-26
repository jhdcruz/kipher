/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.chacha

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

/**
 * Data encryption using ChaCha20.
 */
class ChaCha20 : StandardEncryption(ALGORITHM, MODE) {
    override val ivLength: Int = IV_LENGTH

    companion object {
        private val MODE: String = ChaChaModes.ChaCha20.mode
    }
}

