/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherEncryption
import org.jetbrains.annotations.NotNull
import java.security.Provider
import javax.crypto.Cipher

// Constants
internal const val ALGORITHM: String = "AES"
internal const val AES_BLOCK_SIZE: Int = 128

/**
 * Provides common functionalities for AES encryption.
 *
 * Should not be used/consumed directly. Unless for changing provider
 *
 * @param aesMode AES mode used for encryption.
 */
sealed class AesEncryption(@NotNull aesMode: AesModes) : KipherEncryption(ALGORITHM, provider) {

    internal val cipher: Cipher = Cipher.getInstance(aesMode.mode)

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null
    }
}
