/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

/** Available hash [mode]s. */
enum class MacModes(val mode: String) {
    HmacMD5("HmacMD5"),

    HmacSHA512("HmacSHA512"),
    HmacSHA384("HmacSHA384"),
    HmacSHA256("HmacSHA256"),
    HmacSHA224("HmacSHA224"),
}
