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
 * AES Encryption using authenticated modes.
 *
 * It incorporates a tag that provides a cryptographic checksum that can be used to help validate a
 * decryption such as additional clear text, or associated data, into the tag used for validation
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @property aesMode Custom AES mode from [AesModes].
 * @property keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
open class AuthenticatedEncryption @JvmOverloads constructor(
    private val aesMode: AesModes,
    private val keySize: Int = DEFAULT_KEY_SIZE,
) : AesEncryption(keySize) {
    override val cipher: Cipher = Cipher.getInstance(aesMode.mode, "BC")

    /**
     * Encrypts the provided [data] along with [aad] using [key].
     *
     * Returns both IV and cipher text separately in a [Pair] (iv, data).
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encryptWithIv(
        data: ByteArray,
        aad: ByteArray,
        key: ByteArray,
    ): Pair<ByteArray, ByteArray> {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()
            val keySpec = SecretKeySpec(key, ALGORITHM)

            val parameterSpec = GCMParameterSpec(AUTHENTICATED_TAG_LENGTH, iv)

            cipher.run {
                init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
                updateAAD(aad) // attach additional data
                doFinal(data)
            }.let { cipherText ->
                Pair(iv, cipherText)
            }
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
    fun decrypt(
        encrypted: ByteArray,
        aad: ByteArray,
        key: ByteArray,
        iv: ByteArray? = null,
    ): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            if (iv != null) {
                // use the provided iv
                val gcmIv = GCMParameterSpec(AUTHENTICATED_TAG_LENGTH, iv)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)
            } else {
                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(
                    AUTHENTICATED_TAG_LENGTH,
                    encrypted,
                    0,
                    AUTHENTICATED_IV_LENGTH,
                )
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
                    AUTHENTICATED_IV_LENGTH,
                    encrypted.size - AUTHENTICATED_IV_LENGTH,
                )
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }
}
