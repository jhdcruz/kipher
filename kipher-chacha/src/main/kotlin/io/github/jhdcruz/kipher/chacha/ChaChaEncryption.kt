/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

import io.github.jhdcruz.kipher.common.KipherEncryption
import io.github.jhdcruz.kipher.common.KipherException
import org.jetbrains.annotations.NotNull
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import java.security.Provider
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

internal const val ALGORITHM: String = "ChaCha20"
internal const val NONCE_LENGTH: Int = 12

sealed class ChaChaEncryption(chachaMode: ChaChaModes) : KipherEncryption(ALGORITHM, provider) {

    @Suppress("MemberVisibilityCanBePrivate")
    internal val cipher: Cipher = Cipher.getInstance(chachaMode.mode)

    /**
     * Generates a random 12 byte `nonce`. [96-bit]
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun generateIv() = generateIv(NONCE_LENGTH)

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * This is useful for **advanced use cases** if you want finer control.
     *
     * If you want to encrypt data without worrying about `nonce` and `key,
     * use [encrypt] instead.
     *
     * This does not return with [key] since it's supposed to be provided,
     * unlike [encrypt] which generates a new key for each encryption.
     *
     * @return [Map] containing the `data`, `nonce`, and `key`
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun encryptBare(
        @NotNull data: ByteArray,
        @NotNull nonce: ByteArray,
        @NotNull key: ByteArray,
    ): Map<String, ByteArray> {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.run {
                init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(nonce))
                doFinal(data)
            }.let { cipherText ->
                mapOf(
                    "data" to cipherText,
                    "nonce" to nonce,
                )
            }
        } catch (e: GeneralSecurityException) {
            throw KipherException("Error encrypting file", e)
        }
    }

    /**
     * Decrypts [encrypted] data using [key] and optional [nonce].
     *
     * @return Decrypted data
     * @throws KipherException
     */
    @Throws(KipherException::class)
    fun decryptBare(
        @NotNull encrypted: ByteArray,
        @NotNull nonce: ByteArray,
        @NotNull key: ByteArray,
    ): ByteArray {
        return try {
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.run {
                init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(nonce))
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
            nonce = generateIv(),
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
     * This method assumes that the [encrypted] data is in `[nonce, data]` format,
     * presumably encrypted using [encrypt]
     */
    fun decrypt(
        @NotNull encrypted: ByteArray,
        @NotNull key: ByteArray,
    ): ByteArray {
        encrypted.extract().let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                nonce = data.getValue("nonce"),
                key = key,
            )
        }
    }

    /**
     * Decrypts [encrypted] data in a [Map] format.
     *
     * This method assumes that [encrypted] is a [Map] that contains
     * concatenated encrypted data in `[nonce, data, aad]` format
     * and `key`, presumably encrypted using [encrypt]
     */
    fun decrypt(@NotNull encrypted: Map<String, ByteArray>): ByteArray {
        val key = encrypted.getValue("key")
        val concatData = encrypted.getValue("data")

        concatData.extract().let { data ->
            return decryptBare(
                encrypted = data.getValue("data"),
                nonce = data.getValue("nonce"),
                key = key,
            )
        }
    }

    /**
     * Concatenate the encryption details from a [Map] data.
     *
     * @return [ByteArray] Concatenated data in `[nonce, data]` format
     */
    @Throws(KipherException::class)
    override fun Map<String, ByteArray>.concat(): ByteArray {
        return try {
            val encryptedSize = this.values.sumOf { it.size }
            val context = this // reference inside run

            // concatenate nonce, cipher text
            ByteBuffer.allocate(encryptedSize).run {
                put(context["nonce"])
                put(context["data"])
                array()
            }
        } catch (e: IndexOutOfBoundsException) {
            throw KipherException("Error concatenating encryption details", e)
        }
    }

    /**
     * Extracts the `nonce` and `data` from the [ByteArray] data.
     *
     * This can only extract data encrypted using [encrypt] or [encryptBare]
     *
     * If you want to extract data encrypted outside of `Kipher`, you have to extract them
     * manually based on how they are structured
     *
     * @return [Map] containing the `nonce`, `data`
     */
    @Throws(KipherException::class)
    override fun ByteArray.extract(): Map<String, ByteArray> {
        return try {
            val buffer = ByteBuffer.wrap(this)

            val nonce = ByteArray(NONCE_LENGTH)
            val cipherText = ByteArray(buffer.remaining() - NONCE_LENGTH)

            mapOf(
                "nonce" to buffer.get(nonce).array(),
                "data" to buffer.get(cipherText).array(),
            )
        } catch (e: Exception) {
            throw KipherException("Error extracting encryption details from provided file", e)
        }
    }

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null
    }
}
