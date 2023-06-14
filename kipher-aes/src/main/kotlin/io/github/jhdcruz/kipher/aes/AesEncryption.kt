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
internal const val BASIC_IV_LENGTH = 16
internal const val AUTHENTICATED_TAG_LENGTH = 128
internal const val AUTHENTICATED_IV_LENGTH = 12

/**
 * Provides common functionalities for AES encryption.
 *
 * Should not be used/consumed directly.
 *
 * @property keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
sealed class AesEncryption(private val keySize: Int) : BaseEncryption() {
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    /** Generate a random iv based on encryption mode used. */
    internal fun generateIv(): ByteArray {
        return when (this) {
            is BasicEncryption -> ByteArray(BASIC_IV_LENGTH).also {
                randomize.nextBytes(it)
            }

            is AuthenticatedEncryption -> ByteArray(AUTHENTICATED_IV_LENGTH).also {
                randomize.nextBytes(it)
            }
        }
    }

    /** Generate a secret key. */
    fun generateKey(): ByteArray {
        return keyGenerator.run {
            init(keySize)
            generateKey().encoded
        }
    }

    companion object {
        /** Default key size value for aes encryption */
        const val DEFAULT_KEY_SIZE: Int = 256
    }
}
