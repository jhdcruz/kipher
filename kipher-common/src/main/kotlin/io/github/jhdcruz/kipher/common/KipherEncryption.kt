/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.common

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.security.Provider
import java.security.SecureRandom
import javax.crypto.KeyGenerator

internal const val DEFAULT_KEY_SIZE: Int = 256

/**
 * Base class for encryption.
 *
 * @param algorithm Encryption algorithm to use. (e.g. AES, ChaCha20)
 * @param provider Security provider to use.
 */
abstract class KipherEncryption(
    @NotNull algorithm: String,
    @Nullable provider: Provider?,
) : KipherProvider(provider) {
    private val randomize = SecureRandom()
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(algorithm)

    /**
     * Generate IV based on [length].
     *
     * Can be used as nonce.
     */
    fun generateIv(@NotNull byteLength: Int): ByteArray {
        return ByteArray(byteLength).also {
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
        /** Set JCE security provider. */
        var provider: Provider? = null
    }
}
