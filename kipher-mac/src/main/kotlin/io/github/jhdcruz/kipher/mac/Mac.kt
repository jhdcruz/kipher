/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

import io.github.jhdcruz.kipher.common.Format.toHexString
import io.github.jhdcruz.kipher.common.KipherException
import io.github.jhdcruz.kipher.common.KipherProvider
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.security.InvalidKeyException
import java.security.Provider
import java.security.SecureRandom
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
class Mac(@NotNull val macMode: MacModes) : KipherProvider(provider) {
    private val mode = macMode.mode
    private val randomize = SecureRandom()

    /**
     * Allows you to set custom salt length per instance.
     */
    var saltLength: Int = Companion.saltLength

    /**
     * Set custom iterations per instance.
     */
    var iterations: Int = Companion.iterations

    private fun generateSalt(): ByteArray {
        return ByteArray(saltLength).also {
            randomize.nextBytes(it)
        }
    }

    /**
     * Generate a password-based encryption key
     * based on [keyMode] and [password].
     */
    fun generateKey(
        @NotNull keyMode: KeyModes,
        @Nullable password: String,
    ): ByteArray {
        val keyFactory = SecretKeyFactory.getInstance(keyMode.mode)
        val salt = generateSalt()

        val keySpec = PBEKeySpec(
            password.toCharArray(),
            salt,
            iterations,
            keyMode.length,
        )

        val secretKey = keyFactory.generateSecret(keySpec)

        return secretKey.encoded
    }

    /**
     * Generate a key based on current mode,
     * or by providing a custom [keyLength].
     *
     * For using PBKDF2 key with HMACs, provide [KeyModes] instead.
     */
    @JvmOverloads
    fun generateKey(@Nullable keyLength: Int? = null): ByteArray {
        val key = ByteArray(keyLength ?: mode.length).also {
            randomize.nextBytes(it)
        }

        val keySpec = SecretKeySpec(
            key,
            mode,
        )

        return keySpec.encoded
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
            val mac = Mac.getInstance(mode)
            val secretKey = SecretKeySpec(key, mode)

            mac.init(secretKey)

            // for multiple data authentication
            for (item in data) {
                mac.update(item)
            }

            mac.doFinal()
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
     * Verify if [data] matches [expected] hash using provided [key].
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull expected: ByteArray,
        @NotNull key: ByteArray,
    ): Boolean = generateHash(data, key).contentEquals(expected)

    /**
     * Verify if [data] matches [expected] hash using provided [key].
     *
     * [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: List<ByteArray>,
        @NotNull expected: ByteArray,
        @NotNull key: ByteArray,
    ): Boolean = generateHash(data, key).contentEquals(expected)

    /**
     * Verify if [data] matches [expected] hash using provided [key].
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull expected: String,
        @NotNull key: ByteArray,
    ): Boolean = generateHash(data, key).toHexString().contentEquals(expected)

    /**
     * Verify if multiple [data] matches [expected] hash using provided [key].
     *
     * list of [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: List<ByteArray>,
        @NotNull expected: String,
        @NotNull key: ByteArray,
    ): Boolean = generateHash(data, key).toHexString().contentEquals(expected)

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null

        /**
         * Allows you to set custom salt length.
         *
         * Default: `32`
         */
        @Suppress("MagicNumber")
        var saltLength: Int = 32

        /**
         *  Set custom iterations.
         *
         *  Default: `250,000`
         */
        @Suppress("MagicNumber")
        var iterations: Int = 250_000
    }
}
