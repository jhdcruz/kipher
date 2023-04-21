/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.aes

import kipher.common.KipherException
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES Encryption using CBC mode.
 *
 * The Initialization Vector (IV) is generated randomly and prepended to the cipher text.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
class CbcEncryption(
    private val keySize: Int = DEFAULT_KEY_SIZE,
) : AesEncryption(keySize, AesModes.CBC) {
    // use iv length based on current mode
    override val ivLength: Int = IV_LENGTH

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))

            val cipherText = cipher.doFinal(data)

            // concatenate iv and cipher text
            ByteBuffer.allocate(iv.size + cipherText.size).apply {
                put(iv)
                put(cipherText)
            }.array()
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }

    /**
     * Decrypts [encrypted] data using [key].
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(encrypted, 0, ivLength))

            // Use everything from 12 bytes on as ciphertext
            cipher.doFinal(
                encrypted,
                ivLength,
                encrypted.size - ivLength,
            )
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }
}
