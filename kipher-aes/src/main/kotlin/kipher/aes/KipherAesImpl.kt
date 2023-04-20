/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

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

sealed class KipherAesImpl(aesMode: AesModes) : BaseEncryption() {
    override val cipher: Cipher = Cipher.getInstance(aesMode.mode, "BC")

    internal val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    abstract val ivLength: Int

    internal abstract fun generateIv(): ByteArray

    /** Generate a secret key. */
    abstract fun generateKey(): ByteArray

    /** Encrypts the provided [data] using the provided [key]. */
    abstract fun encrypt(data: ByteArray, key: ByteArray): ByteArray

    /** Decrypts [encrypted] data using [key]. */
    abstract fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray
}
