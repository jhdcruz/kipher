/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

import io.github.jhdcruz.kipher.core.Format.toHexString
import io.github.jhdcruz.kipher.core.KipherException
import io.github.jhdcruz.kipher.core.KipherProvider
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.security.InvalidKeyException
import java.security.Provider
import java.security.SecureRandom
import javax.crypto.KeyGenerator
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Data authenticity and integrity checks using
 * Keyed hash-based message authentication code (HMACs).
 *
 * @property macMode [MacModes] to be used for HMAC operations.
 */
class Mac(@NotNull val macMode: MacModes) : KipherProvider(provider) {
    private val randomize = SecureRandom()

    private val algorithm: String = macMode.algorithm ?: macMode.mode

    /**
     * Generate a key based on current macMode.mode,
     * or by providing a custom [keyLength].
     */
    @JvmOverloads
    fun generateKey(@Nullable keyLength: Int? = macMode.keySize): ByteArray {
        return KeyGenerator.getInstance(algorithm).run {
            if (keyLength != null) {
                init(keyLength, randomize)
            } else {
                // use the default
                init(randomize)
            }

            generateKey().encoded
        }
    }

    /**
     *  Generate HMAC for multiple [data] using provided [key].
     *
     *  If you want a string representation use [generateMacString]
     *  instead, or manually invoke [toHexString] on the output.
     */
    @Throws(KipherException::class)
    fun generateMac(
        @NotNull data: Iterable<ByteArray>,
        @NotNull key: ByteArray,
    ): ByteArray {
        return try {
            val mac = Mac.getInstance(macMode.mode)
            val secretKey = SecretKeySpec(key, macMode.mode)

            mac.init(secretKey)

            // for multiple data authentication
            for (item in data) {
                mac.update(item)
            }

            mac.doFinal()
        } catch (e: InvalidKeyException) {
            throw KipherException(e)
        } catch (e: IllegalStateException) {
            throw KipherException(e)
        }
    }

    /**
     *  Generate HMAC for [data] using provided [key].
     *
     *  If you want a string representation use [generateMacString]
     *  instead, or manually invoke [toHexString] on the output.
     */
    @Throws(KipherException::class)
    fun generateMac(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray,
    ): ByteArray = generateMac(listOf(data), key)

    /**
     *  Generate HMAC for [data] using provided [key]
     *  in string format.
     */
    fun generateMacString(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray,
    ): String = generateMac(data, key).toHexString()

    /**
     *  Generate HMAC for multiple [data] using provided [key]
     *  in string format.
     */
    fun generateMacString(
        @NotNull data: Iterable<ByteArray>,
        @NotNull key: ByteArray,
    ): String = generateMac(data, key).toHexString()

    /**
     * Verify if [data] matches [expected] hash using provided [key].
     */
    fun verifyMac(
        @NotNull data: ByteArray,
        @NotNull expected: ByteArray,
        @NotNull key: ByteArray,
    ): Boolean = generateMac(data, key).contentEquals(expected)

    /**
     * Verify if [data] matches [expected] hash using provided [key].
     *
     * [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyMac(
        @NotNull data: Iterable<ByteArray>,
        @NotNull expected: ByteArray,
        @NotNull key: ByteArray,
    ): Boolean = generateMac(data, key).contentEquals(expected)

    /**
     * Verify if [data] matches [expected] hash using provided [key].
     */
    fun verifyMac(
        @NotNull data: ByteArray,
        @NotNull expected: String,
        @NotNull key: ByteArray,
    ): Boolean = generateMac(data, key).toHexString().contentEquals(expected)

    /**
     * Verify if multiple [data] matches [expected] hash using provided [key].
     *
     * Iterable of [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyMac(
        @NotNull data: Iterable<ByteArray>,
        @NotNull expected: String,
        @NotNull key: ByteArray,
    ): Boolean = generateMac(data, key).toHexString().contentEquals(expected)

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null
    }
}
