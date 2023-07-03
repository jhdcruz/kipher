/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.hash.mac

import io.github.jhdcruz.kipher.common.KipherException
import io.github.jhdcruz.kipher.common.KipherProvider
import io.github.jhdcruz.kipher.hash.HashUtils.toHexString
import org.jetbrains.annotations.NotNull
import java.security.InvalidKeyException
import java.security.Provider
import java.security.SecureRandom
import java.util.*
import javax.crypto.Mac
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * Data authenticity and integrity checks using
 * Keyed hash-based message authentication code (HMACs).
 *
 * @property macMode [MacModes] to be used for HMAC operations.
 */
@Suppress("MagicNumber")
sealed class Hmac(@NotNull val macMode: MacModes) : KipherProvider(provider) {
    private val mode = macMode.mode
    private val randomize = SecureRandom()

    /**
     * Allows you to set custom salt length per instance.
     */
    var saltLength: Int = Companion.saltLength

    /**
     *  Set custom iterations per instance.
     */
    var iterations: Int = Companion.iterations

    private fun generateSalt(): ByteArray {
        return ByteArray(saltLength).also {
            randomize.nextBytes(it)
        }
    }

    /**
     *  Generate a [password]ed PBKDF2 key
     *  with support for multiple [keyMode]s,
     *  and opt-in support for encryption key.
     */
    @JvmOverloads
    fun generateKey(
        @NotNull keyMode: KeyModes,
        @NotNull password: String,
        @NotNull withEncryption: Boolean = false,
    ): ByteArray {
        val secretKeyFactory = SecretKeyFactory.getInstance(keyMode.mode)
        val salt = generateSalt()

        val keyLength = if (!withEncryption) {
            keyMode.length
        } else {
            keyMode.length * 2 // Double the length for encryption support
        }

        val keySpec = PBEKeySpec(
            password.toCharArray(),
            salt,
            iterations,
            keyLength,
        )

        val secretKey = secretKeyFactory.generateSecret(keySpec)

        return secretKey.encoded
    }

    /**
     *  Generate HMAC for multiple [data] using provided [key].
     *
     *  If you want a string representation use [generateHashString]
     *  instead, or manually invoke [toHexString] on the output.
     */
    @Throws(KipherException::class)
    fun generateHash(
        @NotNull data: List<ByteArray>,
        @NotNull key: ByteArray,
    ): ByteArray {
        return try {
            val hmac = Mac.getInstance(mode)
            val secretKey = SecretKeySpec(key, mode)

            hmac.init(secretKey)

            // for multiple data authentication
            // example usage is IV, and cipher-text from AES encryption
            for (item in data) {
                hmac.update(item)
            }

            hmac.doFinal()
        } catch (e: InvalidKeyException) {
            throw KipherException("inappropriate key for initializing MAC", e)
        } catch (e: IllegalStateException) {
            throw KipherException(e)
        }
    }

    /**
     *  Generate HMAC for [data] using provided [key].
     *
     *  If you want a string representation use [generateHashString]
     *  instead, or manually invoke [toHexString] on the output.
     */
    @Throws(KipherException::class)
    fun generateHash(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray,
    ): ByteArray = generateHash(listOf(data), key)

    /**
     *  Generate HMAC for [data] using provided [key]
     *  in string format.
     */
    fun generateHashString(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray,
    ): String = generateHash(data, key).toHexString()

    /**
     *  Generate HMAC for multiple [data] using provided [key]
     *  in string format.
     */
    fun generateHashString(
        @NotNull data: List<ByteArray>,
        @NotNull key: ByteArray,
    ): String = generateHash(data, key).toHexString()

    /**
     * Verify [hmac] from [data] using provided [key].
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull hmac: ByteArray,
        @NotNull key: ByteArray,
    ): Boolean = hmac.contentEquals(generateHash(data, key))

    /**
     * Verify [hmac] from multiple [data] using provided [key].
     *
     * [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: List<ByteArray>,
        @NotNull hmac: ByteArray,
        @NotNull key: ByteArray,
    ): Boolean = hmac.contentEquals(generateHash(data, key))

    /**
     * Verify [hmac] from [data] using provided [key].
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull hmac: String,
        @NotNull key: ByteArray,
    ): Boolean = hmac.contentEquals(generateHash(data, key).toHexString())

    /**
     * Verify [hmac] from multiple [data] using provided [key].
     *
     * [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: List<ByteArray>,
        @NotNull hmac: String,
        @NotNull key: ByteArray,
    ): Boolean = hmac.contentEquals(generateHash(data, key).toHexString())

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null

        /**
         * Allows you to set custom salt length.
         *
         * Default: `32`
         */
        var saltLength: Int = 32

        /**
         *  Set custom iterations.
         *
         *  Default: `250,000`
         */
        var iterations: Int = 250_000
    }
}
