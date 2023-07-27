/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

import io.github.jhdcruz.kipher.symmetric.AEAD

const val AUTHENTICATED_IV_LENGTH: Int = 12

/**
 * AES Encryption using authenticated modes.
 *
 * @param aesMode Authenticated AES mode from [AesModes.AEAD].
 */
sealed class AesAEAD(aesMode: AesModes.AEAD) : AEAD(ALGORITHM, aesMode.mode) {
    override val ivLength: Int = AUTHENTICATED_IV_LENGTH

    override var defaultKeySize: Int? = DEFAULT_KEY_SIZE
}
