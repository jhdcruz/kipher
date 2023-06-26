/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

/** Available PBKDF2 key [mode]s and their bit [length]s. */
@Suppress("MagicNumber")
enum class KeyModes(val mode: String, val length: Int) {
    PBKDF2WithHmacSHA256("PBKDF2WithHmacSHA256", 32),
    PBKDF2WithHmacSHA384("PBKDF2WithHmacSHA384", 48),
    PBKDF2WithHmacSHA512("PBKDF2WithHmacSHA512", 64),
}
