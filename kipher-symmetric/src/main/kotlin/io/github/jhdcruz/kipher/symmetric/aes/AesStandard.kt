/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

import io.github.jhdcruz.kipher.symmetric.StandardEncryption

const val BASIC_IV_LENGTH: Int = 16

/**
 * AES Encryption using basic modes.
 *
 * @param aesMode Basic AES mode from [AesModes.Standard].
 */
sealed class AesStandard(aesMode: AesModes.Standard) : StandardEncryption(ALGORITHM, aesMode.mode) {
    override val ivLength: Int = BASIC_IV_LENGTH
}
