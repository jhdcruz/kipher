/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * Sample AES parameters for testing.
 */
internal object AesParams {
    val message = "message".encodeToByteArray()
    val aad = "metadata".encodeToByteArray()
    val invalidKey = "invalid-key".encodeToByteArray()

    fun decodeToString(bytes: ByteArray): String {
        return String(bytes, Charsets.UTF_8)
    }
}
