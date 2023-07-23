/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.encryption.symmetric

import org.jetbrains.annotations.NotNull
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Basic Encryption using basic modes.
 *
 * @param algorithm algorithm to use for KeyGenerator (e.g. AES, ChaCha20).
 * @param mode algorithm mode to use (e.g. AES/GCM/NoPadding, ChaCha20-Poly1305).
 */
abstract class StandardEncryption(
    @NotNull val algorithm: String,
    @NotNull mode: String,
) : SymmetricEncryption(algorithm, mode) {

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
     */
    fun encryptBare(
        @NotNull data: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray,
    ): Map<String, ByteArray> {
        val keySpec = SecretKeySpec(key, algorithm)

        return cipher.run {
            init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
            doFinal(data)
        }.let { cipherText ->
            mapOf(
                "data" to cipherText,
                "iv" to iv,
            )
        }
    }

    /**
     * Decrypts [encrypted] data using [key] and optional [iv].
     *
     * @return Decrypted data
     */
    fun decryptBare(
        @NotNull encrypted: ByteArray,
        @NotNull iv: ByteArray,
        @NotNull key: ByteArray,
    ): ByteArray {
        val keySpec = SecretKeySpec(key, algorithm)

        return cipher.run {
            init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(iv))
            doFinal(encrypted)
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
            key = key,
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
        @NotNull key: ByteArray,
    ): ByteArray {
        encrypted.extract().let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key,
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

        concatData.extract().let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                iv = data.getValue("iv"),
                key = key,
            )
        }
    }
}
