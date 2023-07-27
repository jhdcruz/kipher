/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.salsa

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

private const val IV_LENGTH: Int = 8

/**
 * Data encryption using ChaCha20.
 */
class Salsa20 : StandardEncryption(ALGORITHM, SalsaModes.Salsa20.mode) {
    override val ivLength: Int = IV_LENGTH
}
