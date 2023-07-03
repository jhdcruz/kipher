/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.hash

import java.util.Base64

/**
 * Helper methods for hash functions.
 */
object HashUtils {

    /** Convert [ByteArray] hash to hexadecimal [String]. */
    fun ByteArray.toHexString(): String {
        val builder = StringBuilder()

        for (byte in this) {
            builder.append(
                String.format("%02x", byte)
            )
        }

        return builder.toString()
    }

    /** Convert [ByteArray] hash to base64 [String]. */
    fun ByteArray.toBase64String(): String {
        return Base64.getEncoder().encodeToString(this)
    }
}
