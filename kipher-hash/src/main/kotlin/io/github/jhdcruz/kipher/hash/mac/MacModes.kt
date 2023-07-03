/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.hash.mac

/** Available hash [mode]s. */
enum class MacModes(val mode: String, val length: Int) {
    HmacMD5("HmacMD5", 16),

    HmacSHA512("HmacSHA512", 64),
    HmacSHA384("HmacSHA384", 48),
    HmacSHA256("HmacSHA256", 32),
    HmacSHA224("HmacSHA224", 28),
}
