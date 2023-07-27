/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

@Suppress("unused")
/**
 * Available hash [mode]s.
 *
 * Set [algorithm] to `null` if its the same as mode.
 *
 * Set [keySize] to `null` to use default, since some
 * requires custom key size and the default is invalid.
 */
enum class MacModes(
    @Nullable val algorithm: String?,
    @NotNull val mode: String,
    @Nullable val keySize: Int?,
) {
    HmacGOST(null, "HmacGOST3411", null),

    HmacRIPEMD128(null, "HmacRIPEMD128", null),
    HmacRIPEMD160(null, "HmacRIPEMD160", null),

    HmacMD5(null, "HmacMD5", null),

    HmacSHA1(null, "HmacSHA1", null),
    HmacSHA224(null, "HmacSHA224", null),
    HmacSHA256(null, "HmacSHA256", null),
    HmacSHA384(null, "HmacSHA384", null),
    HmacSHA512(null, "HmacSHA512", null),

    HmacTiger(null, "HmacTiger", null),
    HmacWhirlpool(null, "HmacWhirlpool", null),

    Kmac128(null, "KMAC128", null),
    Kmac256(null, "KMAC256", null),

    Poly1305(null, "Poly1305", null),

    /*
     * Specialized MACs
     *
     * Mostly used with other ciphers.
     */
    Poly1305_AES("AES", "Poly1305", 256),

    AesCMAC("AES", "AESCMAC", null),

    Threefish_256CMAC("Threefish-256", "Threefish-256CMAC", null),
    Threefish_512CMAC("Threefish-512", "Threefish-512CMAC", null),
    Threefish_1024CMAC("Threefish-1024", "Threefish-1024CMAC", null),
}
