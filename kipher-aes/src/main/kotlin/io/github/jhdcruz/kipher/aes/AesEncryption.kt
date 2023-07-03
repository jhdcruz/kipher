/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

@file:JvmSynthetic

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherException
import io.github.jhdcruz.kipher.common.KipherProvider
import org.jetbrains.annotations.NotNull
import java.security.InvalidParameterException
import java.security.Provider
import java.security.SecureRandom
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
sealed class AesEncryption(@NotNull aesMode: AesModes) : KipherProvider(provider) {
    private val randomize = SecureRandom()
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    internal val cipher: Cipher = Cipher.getInstance(aesMode.mode)

    /**
     * Generate a random IV based on [length].
     */
    fun generateIv(@NotNull length: Int): ByteArray {
        return ByteArray(length).also {
            randomize.nextBytes(it)
        }
    }

    /**
     * Generate a secret key.
     *
     * Reusing the same key for multiple encryption is not recommended,
     * and poses security risk.
     *
     * `encrypt()` functions already generates a new key for each encryption.
     */
    @Throws(KipherException::class)
    fun generateKey(@NotNull keySize: Int = DEFAULT_KEY_SIZE): ByteArray {
        return try {
            keyGenerator.run {
                init(keySize, randomize)
                generateKey().encoded
            }
        } catch (e: InvalidParameterException) {
            throw KipherException("Invalid key size given", e)
        }
    }

    /**
     * Concatenate the encryption details from a [Map] data.
     *
     * @return [ByteArray] Concatenated data
     */
    abstract fun Map<String, ByteArray>.concat(): ByteArray

    /**
     * Extracts the encryption details from the [ByteArray] data.
     *
     * Presumably encrypted using `encrypt()` functions
     *
     * @return [Map] of encryption details (such as iv, data, etc.)
     */
    abstract fun ByteArray.extract(): Map<String, ByteArray>

    companion object {
        /** Default key size value. */
        const val DEFAULT_KEY_SIZE: Int = 256

        /** Set JCE security provider. */
        var provider: Provider? = null
    }
}
