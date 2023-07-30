/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

internal const val ALGORITHM: String = "AES"
internal const val DEFAULT_KEY_SIZE: Int = 256

/** Supported AES encryption modes. */
enum class AesModes {

    /** Supported AES basic encryption [mode]s. */
    enum class Standard(val mode: String) {
        /** Compatibility with SunJCE's PCKS7 Padding. */
        CBC("AES/CBC/PKCS5Padding"),

        CBC7("AES/CBC/PKCS7Padding"),
        CTR("AES/CTR/NoPadding"),
        CFB("AES/CFB/NoPadding"),
    }

    /** Supported AES authenticated encryption [mode]s. */
    enum class AEAD(val mode: String) {
        GCM("AES/GCM/NoPadding"),
        GCM_SIV("AES/GCM-SIV/NoPadding"),
        EAX("AES/EAX/NoPadding"),
        CCM("AES/CCM/NoPadding"),
    }
}
