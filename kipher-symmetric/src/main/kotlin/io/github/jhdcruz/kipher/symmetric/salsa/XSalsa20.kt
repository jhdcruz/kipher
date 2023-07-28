/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.salsa

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

private const val IV_LENGTH: Int = 24

/**
 * Data encryption using Salsa20.
 */
class XSalsa20 : StandardEncryption(ALGORITHM, SalsaModes.XSalsa20.mode) {
    override val ivLength: Int = IV_LENGTH
}
