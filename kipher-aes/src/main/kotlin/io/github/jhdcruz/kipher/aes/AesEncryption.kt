/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

@file:JvmSynthetic

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.BaseEncryption
import org.jetbrains.annotations.NotNull
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

// Constants
internal const val ALGORITHM = "AES"
internal const val AES_BLOCK_SIZE = 128

/**
 * Provides common functionalities for AES encryption.
 *
 * Should not be used/consumed directly.
 *
 * @param aesMode AES mode used for encryption.
 */
sealed class AesEncryption(@NotNull aesMode: AesModes) : BaseEncryption() {
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    override val cipher: Cipher = Cipher.getInstance(aesMode.mode, "BC")

    // Generate a random iv based on encryption mode used.
    internal abstract fun generateIv(): ByteArray

    /** Generate a secret key.
     *
     * Reusing the same key for multiple encryption is not recommended,
     * and poses security risk.
     *
     * `encrypt()` functions already generates a new key for each encryption.
     */
    fun generateKey(@NotNull keySize: Int = DEFAULT_KEY_SIZE): ByteArray {
        return keyGenerator.run {
            init(keySize, randomize)
            generateKey().encoded
        }
    }

    companion object {
        /** Default key size value */
        const val DEFAULT_KEY_SIZE: Int = 256
    }
}
