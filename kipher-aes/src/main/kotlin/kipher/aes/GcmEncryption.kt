/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.aes

import kipher.common.KipherException
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * AES Encryption using GCM mode.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
class GcmEncryption(
    private val keySize: Int = DEFAULT_KEY_SIZE,
) : AesEncryption(keySize, AesModes.GCM) {

    // use iv length based on current mode
    override val ivLength: Int = GCM_IV_LENGTH

    /**
     * Encrypts the provided [data] along with [aad] using [key].
     *
     * Returns both IV and cipher text separately in a [Pair] (iv, data).
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encryptWithIv(data: ByteArray, aad: ByteArray, key: ByteArray): Pair<ByteArray, ByteArray> {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()
            val keySpec = SecretKeySpec(key, ALGORITHM)

            val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)

            // encrypt data along with the aad
            cipher.updateAAD(aad)
            val cipherText = cipher.doFinal(data)

            Pair(iv, cipherText)
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * IV is prepended to the cipher text.
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encrypt(data: ByteArray, aad: ByteArray, key: ByteArray): ByteArray {
        return try {
            encryptWithIv(data, aad, key).let { (iv, cipherText) ->
                // concatenate iv and cipher text
                ByteBuffer.allocate(iv.size + cipherText.size).apply {
                    put(iv)
                    put(cipherText)
                }.array()
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }

    /**
     * Decrypts [encrypted] data with [aad] verification using [key].
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    @JvmOverloads
    fun decrypt(encrypted: ByteArray, aad: ByteArray, key: ByteArray, iv: ByteArray? = null): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            if (iv != null) {
                // use the provided iv
                val gcmIv = GCMParameterSpec(GCM_TAG_LENGTH, iv)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)
            } else {
                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(GCM_TAG_LENGTH, encrypted, 0, ivLength)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)
            }

            // verify aad of the encrypted data
            cipher.updateAAD(aad)

            if (iv != null) {
                cipher.doFinal(encrypted)
            } else {
                // Use everything from 12 bytes on as ciphertext
                cipher.doFinal(
                    encrypted,
                    ivLength,
                    encrypted.size - ivLength,
                )
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }
}
