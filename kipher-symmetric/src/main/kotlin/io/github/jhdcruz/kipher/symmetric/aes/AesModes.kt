/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

internal const val ALGORITHM: String = "AES"

/** Available AES encryption modes. */
enum class AesModes {
    ;

    /** AES Basic encryption modes. */
    enum class Standard(val mode: String) {
        /** Compatibility with SunJCE's PCKS7 Padding. */
        CBC("AES/CBC/PKCS5Padding"),

        CBC7("AES/CBC/PKCS7Padding"),
        CTR("AES/CTR/NoPadding"),
        CFB("AES/CFB/NoPadding"),
    }

    /** AES Authenticated encryption modes. */
    enum class AEAD(val mode: String) {
        GCM("AES/GCM/NoPadding"),
        CCM("AES/CCM/NoPadding"),
    }
}
