/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/** Available AES encryption [mode]s */
enum class AesModes(val mode: String) {
    CBC("AES/CBC/PKCS7Padding"),
    CTR("AES/CTR/NoPadding"),
    CFB("AES/CFB/NoPadding"),

    GCM("AES/GCM/NoPadding"),
    CCM("AES/CCM/NoPadding"),
}
