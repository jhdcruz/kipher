/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherException
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

internal const val AUTHENTICATED_IV_LENGTH: Int = 12

/**
 * AES Encryption using authenticated modes.
 *
 * It incorporates `aad` that provides a cryptographic checksum that can be used to help
 * validate a decryption such as additional clear text, or associated data used for validation
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param aesMode Custom AES mode from [AesModes].
 */
open class AuthenticatedEncryption(aesMode: AesModes) : AesEncryption(aesMode) {

    override fun generateIv(): ByteArray {
        return ByteArray(AUTHENTICATED_IV_LENGTH).also {
            randomize.nextBytes(it)
        }
    }

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

            val parameterSpec = GCMParameterSpec(AES_BLOCK_SIZE, iv)

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

            // use the provided iv, else extract from encrypted
            if (iv != null) {
                val gcmIv = GCMParameterSpec(AES_BLOCK_SIZE, iv)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)
            } else {
                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(
                    AES_BLOCK_SIZE,
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
                // Use everything from 12th bytes on as ciphertext
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
