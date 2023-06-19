/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherException
import org.jetbrains.annotations.NotNull
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal const val BASIC_IV_LENGTH: Int = 16

/**
 * AES Encryption using basic modes.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param aesMode Custom AES mode from [AesModes].
 */
open class BasicEncryption(aesMode: AesModes) : AesEncryption(aesMode) {

    override fun generateIv(): ByteArray {
        return ByteArray(BASIC_IV_LENGTH).also {
            randomize.nextBytes(it)
        }
    }

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * This is useful for **advanced use cases** if you want finer control
     * over what to do with the outputs.
     *
     * If you want to encrypt data without worrying about `iv`, use [encrypt] instead.
     *
     * @return [Map] containing the `data`, `iv`, and `key`
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encryptBare(
        @NotNull data: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray
    ): Map<String, ByteArray> {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.run {
                init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
                doFinal(data)
            }.let { cipherText ->
                mapOf(
                    "data" to cipherText,
                    "iv" to iv,
                    "key" to key,
                )
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException("Error encrypting file", e)
        }
    }

    /**
     * Decrypts [encrypted] data using [key] and optional [iv].
     *
     * @return Decrypted data
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun decryptBare(
        @NotNull encrypted: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray
    ): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            // use the provided iv
            cipher.run {
                init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
                doFinal(encrypted)
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException("Error decrypting file", e)
        }
    }

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * This method already generates a new key for each encryption.
     * [generateKey] is optional.
     *
     * @return [Map] containing the `data` and `key`
     * @return Encrypted data
     */
    @JvmOverloads
    fun encrypt(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray = generateKey(),
    ): Map<String, ByteArray> {
        return encryptBare(
            data = data,
            iv = generateIv(),
            key = key
        ).let { encrypted ->
            // concat iv and data
            val concatData = ByteBuffer.allocate(
                encrypted.getValue("iv").size + encrypted.getValue("data").size
            ).run {
                put(encrypted["iv"])
                put(encrypted["data"])
                array()
            }

            // we also return the key here since key can be
            // optional and automatically be generated for
            // every encryption, if omitted
            mapOf(
                "data" to concatData,
                "key" to key,
            )
        }
    }

    /**
     * Decrypts [encrypted] data using [key].
     *
     * This method assumes that the [encrypted] data is in `[iv, data]` format,
     * presumably encrypted using [encrypt]
     *
     * @throws KipherException
     */
    fun decrypt(
        @NotNull encrypted: ByteArray,
        @NotNull key: ByteArray
    ): ByteArray {
        extract(encrypted).let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key
            )
        }
    }

    /**
     * Extracts the `iv` and `data` from the [encrypted] data.
     *
     * This can only extract data encrypted using [encrypt] or [encryptBare]
     *
     * If you want to extract data encrypted outside of `Kipher`, you have to extract them
     * manually based on how they are structured
     *
     * @return [Map] containing the `iv`, `data`
     */
    @Throws(KipherException::class)
    override fun extract(@NotNull encrypted: ByteArray): Map<String, ByteArray> {
        return try {
            // get iv from the first 12 bytes
            val iv = encrypted.copyOfRange(0, BASIC_IV_LENGTH)
            val cipherText = encrypted.copyOfRange(BASIC_IV_LENGTH, encrypted.size)

            mapOf(
                "iv" to iv,
                "data" to cipherText
            )
        } catch (e: Exception) {
            throw KipherException("Error extracting encryption details from provided file", e)
        }
    }
}
