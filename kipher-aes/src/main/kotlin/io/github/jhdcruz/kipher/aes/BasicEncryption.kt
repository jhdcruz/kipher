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
sealed class BasicEncryption(aesMode: AesModes) : AesEncryption(aesMode) {

    /**
     * Generates a random 16 byte `iv`.
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun generateIv(): ByteArray = generateIv(BASIC_IV_LENGTH)

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * This is useful for **advanced use cases** if you want finer control.
     *
     * If you want to encrypt data without worrying about `iv` and `key,
     * use [encrypt] instead.
     *
     * This does not return with [key] since it's supposed to be provided,
     * unlike [encrypt] which generates a new key for each encryption.
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
                    "iv" to iv
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
        val encrypted = encryptBare(
            data = data,
            iv = generateIv(),
            key = key
        ).concat()

        return mapOf(
            "data" to encrypted,
            "key" to key,
        )
    }

    /**
     * Decrypts [encrypted] data using [key].
     *
     * This method assumes that the [encrypted] data is in `[iv, data]` format,
     * presumably encrypted using [encrypt]
     */
    fun decrypt(
        @NotNull encrypted: ByteArray,
        @NotNull key: ByteArray
    ): ByteArray {
        encrypted
            .extract()
            .let { data ->
                return decryptBare(
                    encrypted = data.getValue("data"),
                    iv = data.getValue("iv"),
                    key = key
                )
            }
    }

    /**
     * Decrypts [encrypted] data in a [Map] format.
     *
     * This method assumes that [encrypted] is a [Map] that contains
     * concatenated encrypted data in `[iv, data, aad]` format
     * and `key`, presumably encrypted using [encrypt]
     */
    fun decrypt(@NotNull encrypted: Map<String, ByteArray>): ByteArray {
        val key = encrypted.getValue("key")
        val concatData = encrypted.getValue("data")

        concatData
            .extract()
            .let { data ->
                return decryptBare(
                    encrypted = data.getValue("data"),
                    iv = data.getValue("iv"),
                    key = key
                )
            }
    }

    /**
     * Concatenate the encryption details from a [Map] data.
     *
     * @return [ByteArray] Concatenated data in `[iv, data]` format
     */
    @Throws(KipherException::class)
    override fun Map<String, ByteArray>.concat(): ByteArray {
        return try {
            val encryptedSize = this.values.sumOf { it.size }
            val context = this // reference inside run

            // concatenate iv, cipher text, and aad
            ByteBuffer.allocate(encryptedSize).run {
                put(context["iv"])
                put(context["data"])
                array()
            }
        } catch (e: IndexOutOfBoundsException) {
            throw KipherException("Error concatenating encryption details", e)
        }
    }

    /**
     * Extracts the `iv` and `data` from the [ByteArray] data.
     *
     * This can only extract data encrypted using [encrypt] or [encryptBare]
     *
     * If you want to extract data encrypted outside of `Kipher`, you have to extract them
     * manually based on how they are structured
     *
     * @return [Map] containing the `iv`, `data`
     */
    @Throws(KipherException::class)
    override fun ByteArray.extract(): Map<String, ByteArray> {
        return try {
            // get iv from the first 12 bytes
            val iv = this.copyOfRange(0, BASIC_IV_LENGTH)
            val cipherText = this.copyOfRange(BASIC_IV_LENGTH, this.size)

            mapOf(
                "iv" to iv,
                "data" to cipherText
            )
        } catch (e: Exception) {
            throw KipherException("Error extracting encryption details from provided file", e)
        }
    }
}
