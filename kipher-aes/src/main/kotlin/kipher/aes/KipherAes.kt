/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.aes

import kipher.common.KipherException
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import java.security.InvalidParameterException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Encryption using AES.
 *
 * The Initialization Vector (IV) is generated randomly and prepended to the cipher text.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 * @param aesMode Custom [AesModes] (default: [AesModes.GCM])
 */
class KipherAes(
    private val keySize: Int = DEFAULT_KEY_SIZE,
    private val aesMode: AesModes = AesModes.GCM,
) : KipherAesImpl(aesMode) {
    // singular constructors of the parameters
    constructor(keySize: Int) : this(keySize, AesModes.GCM)
    constructor(aesMode: AesModes) : this(DEFAULT_KEY_SIZE, aesMode)

    // use iv length based on current mode
    override val ivLength: Int = when (aesMode) {
        AesModes.CBC -> IV_LENGTH
        else -> GCM_IV_LENGTH
    }

    override fun generateIv(): ByteArray {
        // other modes uses 16 iv size while GCM uses 12
        val iv = if (aesMode != AesModes.GCM) {
            ByteArray(IV_LENGTH)
        } else {
            ByteArray(GCM_IV_LENGTH)
        }

        randomize.nextBytes(iv)
        return iv
    }

    /**
     * Generate a secret key.
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    override fun generateKey(): ByteArray {
        return try {
            keyGenerator.run {
                init(keySize)
                generateKey().encoded
            }
        } catch (e: InvalidParameterException) {
            throw KipherException(e)
        }
    }

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    override fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()
            val keySpec = SecretKeySpec(key, ALGORITHM)

            // Only GCM has a specific method compared to other modes
            if (aesMode == AesModes.GCM) {
                val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
            }

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
    override fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            if (aesMode == AesModes.GCM) {
                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(GCM_TAG_LENGTH, encrypted, 0, ivLength)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(encrypted, 0, ivLength))
            }

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

    /**
     * Encrypts the provided [data] along with [aad] using [key].
     *
     * Only supports [AesModes.GCM].
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encrypt(data: ByteArray, aad: ByteArray, key: ByteArray): ByteArray {
        if (aesMode != AesModes.GCM) {
            throw KipherException("Authentication data is only supported in GCM mode.")
        } else {
            return try {
                // randomize iv for each encryption
                val iv = generateIv()
                val keySpec = SecretKeySpec(key, ALGORITHM)

                val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)

                // encrypt data along with the aad
                cipher.updateAAD(aad)
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
    }

    /**
     * Decrypts [encrypted] data with [aad] verification using [key].
     *
     * Only supports [AesModes.GCM].
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun decrypt(encrypted: ByteArray, aad: ByteArray, key: ByteArray): ByteArray {
        if (aesMode != AesModes.GCM) {
            throw KipherException("Authentication data is only supported in GCM mode.")
        } else {
            return try {
                val keySpec = SecretKeySpec(key, ALGORITHM)

                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(GCM_TAG_LENGTH, encrypted, 0, ivLength)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

                // check if aad is correct/matches
                cipher.updateAAD(aad)

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
}
