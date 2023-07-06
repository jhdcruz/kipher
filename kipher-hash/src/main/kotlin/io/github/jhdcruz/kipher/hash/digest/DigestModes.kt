/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.hash.digest

/**
 * Available message digest [mode]s.
 *
 * Availability of modes depends on the provider.
 */
enum class DigestModes(val mode: String) {
    MD5("MD5"),

    SHA_224("SHA224"),
    SHA_256("SHA256"),
    SHA_384("SHA384"),
    SHA_512("SHA512"),

    SHA3_224("SHA3-224"),
    SHA3_256("SHA3-256"),
    SHA3_384("SHA3-384"),
    SHA3_512("SHA3-512"),
}
