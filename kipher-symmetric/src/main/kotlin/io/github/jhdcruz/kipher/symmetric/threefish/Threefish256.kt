/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.threefish

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

private const val ALGORITHM: String = "Threefish-256"

/**
 * Data encryption using Threefish (256-bit variant).
 */
class Threefish256 : StandardEncryption(ALGORITHM, ThreefishModes.Threefish256.mode) {
    override val ivLength: Int = IV_LENGTH
}
