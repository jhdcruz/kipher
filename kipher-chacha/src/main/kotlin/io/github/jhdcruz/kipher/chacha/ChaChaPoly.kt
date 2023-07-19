/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

import io.github.jhdcruz.kipher.common.KipherException
import java.nio.ByteBuffer

internal const val MAC_LENGTH: Int = 16

/**
 * Data encryption using ChaCha20 with Poly1305 MAC
 */
class ChaChaPoly : ChaChaEncryption(ChaChaModes.ChaChaPoly) {

    /**
     * Extracts the `iv`, `mac`, and `data` from the [ByteArray] data.
     *
     * This can only extract data encrypted using [encrypt] or [encryptBare]
     *
     * If you want to extract data encrypted outside of `Kipher`, you have to extract them
     * manually based on how they are structured
     *
     * @return [Map] containing the `iv`, `mac`, `data`
     */
    @Throws(KipherException::class)
    fun ByteArray.extractWithMac(): Map<String, ByteArray> {
        return try {
            val buffer = ByteBuffer.wrap(this)

            val nonce = ByteArray(NONCE_LENGTH)
            // get ciphertext but excluding the last 16 bytes
            val cipherText = ByteArray(buffer.limit() - MAC_LENGTH)
            val mac = ByteArray(MAC_LENGTH)

            mapOf(
                "nonce" to buffer.get(nonce).array(),
                "data" to buffer.get(cipherText).array(),
                "mac" to buffer.get(mac).array()
            )
        } catch (e: Exception) {
            throw KipherException("Error extracting encryption details from provided file", e)
        }
    }
}
