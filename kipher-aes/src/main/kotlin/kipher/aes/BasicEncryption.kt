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
 * AES Encryption using basic modes.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @property keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 * @property aesMode Custom AES mode from [AesModes].
 */
open class BasicEncryption @JvmOverloads constructor(
    private val aesMode: AesModes,
    private val keySize: Int = DEFAULT_KEY_SIZE,
) : AesEncryption(keySize) {
    override val cipher: Cipher = Cipher.getInstance(aesMode.mode, "BC")

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * Returns both IV and cipher text separately in a [Pair] (iv, data).
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encryptWithIv(data: ByteArray, key: ByteArray): Pair<ByteArray, ByteArray> {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.run {
                init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
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
    fun encrypt(data: ByteArray, key: ByteArray): ByteArray {
        return try {
            encryptWithIv(data, key).let { (iv, cipherText) ->
                // prepend iv to cipher text
                ByteBuffer.allocate(BASIC_IV_LENGTH + cipherText.size)
                    .put(iv)
                    .put(cipherText)
                    .array()
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }

    /**
     * Decrypts [encrypted] data using [key] and optional [iv].
     *
     * Provide [iv] if it was not prepended to the cipher text. Otherwise, its optional.
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    @JvmOverloads
    fun decrypt(encrypted: ByteArray, key: ByteArray, iv: ByteArray? = null): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            if (iv != null) {
                // use the provided iv
                cipher.run {
                    init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
                    doFinal(encrypted)
                }
            } else {
                // Use everything from 16 bytes on as ciphertext
                cipher.run {
                    init(
                        Cipher.DECRYPT_MODE,
                        keySpec,
                        IvParameterSpec(encrypted, 0, BASIC_IV_LENGTH),
                    )

                    doFinal(
                        encrypted,
                        BASIC_IV_LENGTH,
                        encrypted.size - BASIC_IV_LENGTH,
                    )
                }
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }
}
