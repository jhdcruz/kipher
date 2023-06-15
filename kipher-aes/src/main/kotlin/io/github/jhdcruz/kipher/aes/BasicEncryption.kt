/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherException
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal const val BASIC_IV_LENGTH: Int = 12

/**
 * AES Encryption using basic modes.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @property aesMode Custom AES mode from [AesModes].
 * @property keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
open class BasicEncryption @JvmOverloads constructor(
    private val aesMode: AesModes,
    private val keySize: Int = DEFAULT_KEY_SIZE,
) : AesEncryption(keySize) {
    override val cipher: Cipher = Cipher.getInstance(aesMode.mode, "BC")

    override fun generateIv(): ByteArray {
        return ByteArray(BASIC_IV_LENGTH).also {
            randomize.nextBytes(it)
        }
    }

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * Returns both IV and cipher text separately in a [Pair] (iv, data).
     *
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encryptWithIv(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray
    ): Pair<ByteArray, ByteArray> {
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
     * @return Encrypted data
     */
    fun encrypt(@NotNull data: ByteArray, @NotNull key: ByteArray): ByteArray {
        return encryptWithIv(data, key).let { (iv, cipherText) ->
            // prepend iv to cipher text
            ByteBuffer.allocate(BASIC_IV_LENGTH + cipherText.size)
                .put(iv)
                .put(cipherText)
                .array()
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
    fun decrypt(
        @NotNull encrypted: ByteArray,
        @NotNull key: ByteArray,
        @Nullable iv: ByteArray? = null
    ): ByteArray {
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
