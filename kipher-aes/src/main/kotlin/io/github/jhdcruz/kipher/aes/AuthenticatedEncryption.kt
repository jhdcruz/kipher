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
     * Encrypts the provided [data] along with [aad] (if provided) using [key].
     *
     * This is useful for **advanced use cases** if you want finer control.
     *
     * If you want to encrypt data without worrying about `iv` and `aad`,
     * use [encrypt] instead
     *
     * This does not return with [key] since it's supposed to be provided,
     * unlike [encrypt] which generates a new key for each encryption.
     *
     * @return [Map] containing the `iv`, `data` (encrypted), and `aad`.
     * @throws KipherException
     */
    @Throws(KipherException::class)
    @JvmOverloads
    fun encryptBare(
        @NotNull data: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf()
    ): Map<String, ByteArray> {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)
            val parameterSpec = GCMParameterSpec(AES_BLOCK_SIZE, iv)

            cipher.run {
                init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)

                // add aad if not empty
                if (aad.isNotEmpty()) {
                    updateAAD(aad)
                }

                doFinal(data)
            }.let { encrypted ->
                mapOf(
                    "data" to encrypted,
                    "iv" to iv,
                    "aad" to aad
                )
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException("Error encrypting file", e)
        }
    }

    /**
     * Decrypts [encrypted] data with optional [aad] verification using [key] nad [iv].
     *
     * @return Decrypted data
     * @throws KipherException
     */
    @Throws(KipherException::class)
    @JvmOverloads
    fun decryptBare(
        @NotNull encrypted: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf(),
    ): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)
            val gcmIv = GCMParameterSpec(AES_BLOCK_SIZE, iv)

            cipher.run {
                init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

                // add aad if not empty
                if (aad.isNotEmpty()) {
                    updateAAD(aad)
                }
                doFinal(encrypted)
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException("Error decrypting file", e)
        }
    }

    /**
     * Encrypts the provided [data] along with optional [aad] and [key].
     *
     * This method already generates a new key for each encryption.
     * [generateKey] is optional.
     *
     * @return Concatenated encrypted data in `[iv, data, aad]` format
     */
    @JvmOverloads
    fun encrypt(
        @NotNull data: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf(),
        @NotNull key: ByteArray = generateKey()
    ): Map<String, ByteArray> {
        val result = encryptBare(
            data = data,
            iv = generateIv(),
            key = key,
            aad = aad
        )

        return try {
            result.let { encrypted ->
                // calculate size of encrypted data based on aad availability
                val encryptedSize = encrypted.values.sumOf { it.size + aadSeparator.size }

                // concatenate iv, cipher text, and aad
                val concatData = ByteBuffer.allocate(encryptedSize).run {
                    put(encrypted["iv"])
                    put(encrypted["data"])

                    // This shouldn't be a problem since AADs are not
                    // supposed to be encrypted or a secret anyway, I guess
                    // https://crypto.stackexchange.com/a/35730
                    put(aadSeparator)
                    put(encrypted["aad"])

                    array()
                }

                // we also return the key here since key can be
                // optional and automatically be generated for
                // every encryption, if omitted
                mapOf(
                    "data" to concatData,
                    "key" to key
                )
            }
        } catch (e: IndexOutOfBoundsException) {
            throw KipherException("Error concatenating encryption details", e)
        }
    }

    /**
     * Decrypts [encrypted] data using [key].
     *
     * This method assumes that the [encrypted] data is in `[iv, data, aad]` format,
     * presumably encrypted using [encrypt]
     */
    fun decrypt(
        @NotNull encrypted: ByteArray,
        @NotNull key: ByteArray,
    ): ByteArray {
        extract(encrypted).let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key,
                aad = data.getValue("aad")
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
    fun decrypt(encrypted: Map<String, ByteArray>): ByteArray {
        val concatData = encrypted.getValue("data")
        val key = encrypted.getValue("key")

        extract(concatData).let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key,
                aad = data.getValue("aad")
            )
        }
    }

    /**
     * Extracts the `iv`, `data`, and `aad` from the [encrypted] data.
     *
     * This can only extract data encrypted using [encrypt] or [encryptBare]
     *
     * If you want to extract data encrypted outside of `Kipher`, you have to extract them
     * manually based on how they are structured
     *
     * @return [Map] containing the `iv`, `data`, and `aad`.
     */
    @Throws(KipherException::class)
    override fun extract(@NotNull encrypted: ByteArray): Map<String, ByteArray> {
        return try {
            // get iv from the first 12 bytes
            val iv = encrypted.copyOfRange(0, AUTHENTICATED_IV_LENGTH)

            // TODO: Refactor or refine finding the AAD, this can
            //       can potentially conflict with an AAD that contains
            //       the same bytes/content as the aad separator.
            //       Probably even encrypted data itself, although quite
            //       unlikely, still possible.

            // find the index of aad and check if its equal
            // bytes as the aadSeparator
            val aadSeparatorIndex = encrypted.indices.first { i ->
                encrypted
                    .sliceArray(i until i + aadSeparator.size)
                    .contentEquals(aadSeparator)
            }

            // get the aad value
            val aad = aadSeparatorIndex.let { aadIndex ->
                val aadValueIndex = aadIndex + aadSeparator.size

                encrypted.copyOfRange(aadValueIndex, encrypted.size)
                    .dropLastWhile { it == 0.toByte() } // trim trailing zeros/spaces
                    .toByteArray()
            }

            val cipherText = encrypted.copyOfRange(AUTHENTICATED_IV_LENGTH, aadSeparatorIndex)

            mapOf(
                "iv" to iv,
                "data" to cipherText,
                "aad" to aad
            )
        } catch (e: Exception) {
            throw KipherException("Error extracting encryption details from provided file", e)
        }
    }

    companion object {
        /** Prefix that separates the data and AAD */
        var aadSeparator: ByteArray = "aad=".toByteArray()
    }
}
