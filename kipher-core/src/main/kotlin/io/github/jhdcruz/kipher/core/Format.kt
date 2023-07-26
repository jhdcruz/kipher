/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.core

import java.util.*

/**
 * Helper methods for formatting.
 */
object Format {

    /** Convert [ByteArray] hash to hexadecimal string. */
    fun ByteArray.toHexString(): String {
        val builder = StringBuilder()

        for (byte in this) {
            builder.append(
                String.format("%02x", byte),
            )
        }

        return builder.toString()
    }

    /** Convert [ByteArray] to Base64 string. */
    fun ByteArray.toBase64(): String {
        return Base64.getEncoder().encodeToString(this)
    }
}
