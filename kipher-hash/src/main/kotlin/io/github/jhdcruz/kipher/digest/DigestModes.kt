/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.digest

/**
 * Available message digest [mode]s.
 *
 * Availability of modes depends on the provider.
 */
enum class DigestModes(val mode: String) {
    MD5("MD5"),

    SHA224("SHA224"),
    SHA256("SHA256"),
    SHA384("SHA384"),
    SHA512("SHA512"),
}
