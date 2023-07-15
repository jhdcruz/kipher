/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

@Suppress("unused", "MagicNumber")
/** Available hash [mode]s. */
enum class MacModes(val mode: String, val length: Int) {
    HmacGOST("HmacGOST3411", 32),

    HmacRIPEMD128("HmacRIPEMD128", 64),
    HmacRIPEMD160("HmacRIPEMD160", 64),

    HmacMD5("HmacMD5", 64),

    HmacSHA1("HmacSHA1", 64),
    HmacSHA224("HmacSHA224", 64),
    HmacSHA256("HmacSHA256", 64),
    HmacSHA384("HmacSHA384", 128),
    HmacSHA512("HmacSHA512", 128),

    HmacTiger("HmacTiger", 64),
    HmacWhirlpool("HmacWhirlpool", 64),

    Kmac128("KMAC128", 64),
    Kmac256("KMAC256", 64),
}
