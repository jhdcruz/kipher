/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.utils

/**
 * Helper methods for formatting.
 */
object Formatters {

    /** Convert [ByteArray] hash to hexadecimal [String]. */
    fun ByteArray.toHexString(): String {
        return String.format("%02x", this)
    }
}
