/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

@file:JvmSynthetic

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.BaseEncryption
import javax.crypto.KeyGenerator

// Constants
internal const val ALGORITHM = "AES"
internal const val AES_BLOCK_SIZE = 128

/**
 * Provides common functionalities for AES encryption.
 *
 * Should not be used/consumed directly.
 *
 * @property keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
sealed class AesEncryption(private val keySize: Int) : BaseEncryption() {
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    // Generate a random iv based on encryption mode used.
    internal abstract fun generateIv(): ByteArray

    /** Generate a secret key.
     *
     * **Reusing the same key for multiple encryption is not recommended,
     * and poses security risk.**
     *
     * `encrypt()` functions already generates a new key for each encryption.
     */
    fun generateKey(): ByteArray {
        return keyGenerator.run {
            init(keySize)
            generateKey().encoded
        }
    }

    companion object {
        /** Default key size value */
        const val DEFAULT_KEY_SIZE: Int = 256
    }
}
