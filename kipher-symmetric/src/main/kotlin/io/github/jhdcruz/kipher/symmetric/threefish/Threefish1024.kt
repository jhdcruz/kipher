/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.threefish

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

private const val ALGORITHM: String = "Threefish-1024"

/**
 * Data encryption using Threefish (1024-bit variant).
 */
class Threefish1024 : StandardEncryption(ALGORITHM, ThreefishModes.Threefish1024.mode) {
    override val ivLength: Int = IV_LENGTH
}
