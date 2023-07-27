/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric

import io.github.jhdcruz.kipher.core.KipherException
import org.jetbrains.annotations.NotNull
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

const val TAG_LENGTH: Int = 128

/**
 * Authenticated Encryption with Associated Data.
 *
 * It incorporates `aad` that provides a cryptographic checksum that can be used to help
 * validate a decryption such as additional clear text, or associated data used for validation
 *
 * @param algorithm algorithm to use for KeyGenerator (e.g. AES, ChaCha20).
 * @param mode algorithm mode to use (e.g. AES/GCM/NoPadding, ChaCha20-Poly1305).
 */
abstract class AEAD(
    @NotNull val algorithm: String,
    @NotNull val mode: String,
) : SymmetricEncryption(algorithm, mode) {

    /**
     * Encrypts the provided [data] along with [aad] (if provided) using [key].
     *
     * This is useful for **advanced use cases** if you want finer control.
     *
     * This does not return with [key] since it's supposed to be provided,
     * unlike [encrypt] which generates a new key for each encryption.
     *
     * @return [Map] containing the `iv` and the encrypted `data`.
     */
    @JvmOverloads
    fun encryptBare(
        @NotNull data: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf(),
    ): Map<String, ByteArray> {
        val keySpec = SecretKeySpec(key, algorithm)
        val parameterSpec = GCMParameterSpec(TAG_LENGTH, iv)

        return cipher.run {
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
            )
        }
    }

    /**
     * Decrypts [encrypted] data with optional [aad] verification using [key] nad [iv].
     *
     * @return Decrypted data
     */
    @JvmOverloads
    fun decryptBare(
        @NotNull encrypted: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf(),
    ): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, algorithm)
            val gcmIv = GCMParameterSpec(TAG_LENGTH, iv)

            cipher.run {
                init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

                // check aad if not empty
                if (aad.isNotEmpty()) {
                    updateAAD(aad)
                }

                doFinal(encrypted)
            }
        } catch (e: AEADBadTagException) {
            throw KipherException(
                "Invalid additional authenticated data (AAD), data might have been tampered.",
                e,
            )
        }
    }

    /**
     * Encrypts the provided [data] along with optional [aad] and [key].
     *
     * This method already generates a new key for each encryption.
     * [generateKey] is optional.
     *
     * If you want to use custom keys, and leave [aad] empty,
     * pass an empty [Byte] instead of `null`.
     *
     * @return Concatenated encrypted data in `[iv, data]` format with `key` and `aad`.
     */
    @JvmOverloads
    fun encrypt(
        @NotNull data: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf(),
        @NotNull key: ByteArray = generateKey(),
    ): Map<String, ByteArray> {
        val encrypted = encryptBare(
            data = data,
            iv = generateIv(),
            key = key,
            aad = aad,
        ).concat()

        return mapOf(
            "data" to encrypted,
            "key" to key,
            "aad" to aad,
        )
    }

    /**
     * Decrypts [encrypted] data using [key] and [aad] if provided.
     *
     * This method assumes that the [encrypted] data is in `[iv, data]` format,
     * presumably encrypted using [encrypt].
     */
    @JvmOverloads
    fun decrypt(
        @NotNull encrypted: ByteArray,
        @NotNull key: ByteArray,
        @NotNull aad: ByteArray = byteArrayOf(),
    ): ByteArray {
        encrypted.extract().let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key,
                aad = aad,
            )
        }
    }

    /**
     * Decrypts [encrypted] data in a [Map] format.
     *
     * This method assumes that [encrypted] is a [Map] that contains
     * concatenated encrypted data in `[iv, data]` format
     * with `key` and `aad`, presumably encrypted using [encrypt].
     */
    fun decrypt(@NotNull encrypted: Map<String, ByteArray>): ByteArray {
        val key = encrypted.getValue("key")
        val aad = encrypted.getValue("aad")
        val concatData = encrypted.getValue("data")

        concatData.extract().let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key,
                aad = aad,
            )
        }
    }
}
