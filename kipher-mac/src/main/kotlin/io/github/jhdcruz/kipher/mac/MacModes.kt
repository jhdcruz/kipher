/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

@Suppress("unused", "MagicNumber")
/** Available hash [mode]s. */
enum class MacModes(val mode: String, val length: Int) {
    HmacGOST("HmacGOST3411", 256),

    HmacRIPEMD128("HmacRIPEMD128", 128),
    HmacRIPEMD160("HmacRIPEMD160", 256),

    HmacMD5("HmacMD5", 128),

    HmacSHA1("HmacSHA1", 160),
    HmacSHA224("HmacSHA224", 224),
    HmacSHA256("HmacSHA256", 256),
    HmacSHA384("HmacSHA384", 384),
    HmacSHA512("HmacSHA512", 512),

    HmacTiger("HmacTiger", 192),
    HmacWhirlpool("HmacWhirlpool", 512),

    Kmac128("KMAC128", 128),
    Kmac256("KMAC256", 256),

    Poly1305("Poly1305", 256),
}
