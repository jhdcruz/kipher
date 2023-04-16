/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.aes

/** AES encryption [mode]s. */
enum class AesModes(val mode: String) {
    /** `AES/GCM/NoPadding` */
    GCM("AES/GCM/NoPadding"),

    /** `AES/CBC/PKCS5Padding` */
    CBC("AES/CBC/PKCS5Padding"),
}
