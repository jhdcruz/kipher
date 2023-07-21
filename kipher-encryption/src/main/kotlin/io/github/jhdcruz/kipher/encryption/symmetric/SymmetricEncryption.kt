/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.encryption.symmetric

import io.github.jhdcruz.kipher.common.KipherException
import io.github.jhdcruz.kipher.encryption.KipherEncryption
import org.jetbrains.annotations.NotNull
import java.nio.ByteBuffer
import java.security.Provider
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

const val DEFAULT_KEY_SIZE: Int = 256

/**
 * Base class for encryption.
 *
 * @param algorithm Encryption algorithm to use. (e.g. AES, ChaCha20)
 */
abstract class SymmetricEncryption(
    @NotNull algorithm: String,
    @NotNull mode: String,
) : KipherEncryption(provider) {
    internal val cipher = Cipher.getInstance(mode)
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(algorithm)

    /**
     * IV/nonce length
     */
    abstract val ivLength: Int

    /**
     * Generate IV based on [ivLength].
     *
     * Can also be used as nonce.
     */
    fun generateIv(): ByteArray {
        return ByteArray(ivLength).also {
            randomize.nextBytes(it)
        }
    }

    /**
     * Generate secret key based on optional key [size].
     *
     * Default: `256`.
     *
     * @param size Key size.
     */
    fun generateKey(@NotNull size: Int = DEFAULT_KEY_SIZE): ByteArray {
        return keyGenerator.run {
            init(size, randomize)
            generateKey().encoded
        }
    }

    /**
     * Concatenate the encryption details from a [Map] data.
     *
     * @return [ByteArray] Concatenated data of `[iv, data]` format
     */
    fun Map<String, ByteArray>.concat(): ByteArray {
        val encryptedSize = this.values.sumOf { it.size }
        val cipherData = this // reference inside ByteBuffer.run

        // concatenate iv, cipher text, and aad
        return ByteBuffer.allocate(encryptedSize).run {
            put(cipherData["iv"])
            put(cipherData["data"])
            array()
        }
    }

    /**
     * Extracts the `iv`, `data` from a [ByteArray] data.
     *
     * If you want to extract data encrypted outside of `Kipher`, you have to extract them
     * manually based on how they are structured.
     *
     * @return [Map] containing the `iv`, `data`.
     */
    @Throws(KipherException::class)
    fun ByteArray.extract(): Map<String, ByteArray> {
        return try {
            val iv = copyOfRange(0, ivLength)
            val cipherText = copyOfRange(ivLength, size)

            mapOf(
                "iv" to iv,
                "data" to cipherText,
            )
        } catch (e: IndexOutOfBoundsException) {
            throw KipherException(
                """
                Error extracting encryption details from provided file
                This encrypted data might not be encrypted using Kipher.
                """,
                e,
            )
        }
    }

    companion object {
        /**
         * JCE provider to use for symmetric encryption.
         *
         * Set to `null` to use the default security provider.
         */
        var provider: Provider? = null
    }
}
