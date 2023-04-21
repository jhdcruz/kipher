/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

@file:JvmSynthetic

package kipher.aes

import kipher.common.BaseEncryption
import javax.crypto.Cipher
import javax.crypto.KeyGenerator

// Constants
internal const val ALGORITHM = "AES"
internal const val IV_LENGTH = 16
internal const val GCM_TAG_LENGTH = 128
internal const val GCM_IV_LENGTH = 12

const val DEFAULT_KEY_SIZE = 256

/**
 * Provides structure for AES encryption.
 *
 * Should not be used/consumed directly.
 */
sealed class AesEncryption(private val keySize: Int, private val aesMode: AesModes) : BaseEncryption() {
    override val cipher: Cipher = Cipher.getInstance(aesMode.mode, "BC")
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    abstract val ivLength: Int

    internal fun generateIv(): ByteArray {
        // other modes uses 16 iv size while GCM uses 12
        val iv = when (aesMode) {
            AesModes.GCM -> ByteArray(GCM_IV_LENGTH)
            else -> ByteArray(IV_LENGTH)
        }.also { randomize.nextBytes(it) }

        return iv
    }

    /** Generate a secret key. */
    fun generateKey(): ByteArray {
        return keyGenerator.run {
            init(keySize)
            generateKey().encoded
        }
    }
}
