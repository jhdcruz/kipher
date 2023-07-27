/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.digest

import io.github.jhdcruz.kipher.core.Format.toHexString
import io.github.jhdcruz.kipher.core.KipherProvider
import org.jetbrains.annotations.NotNull
import java.security.MessageDigest
import java.security.Provider

/**
 * Data authenticity and integrity checks using
 * Cryptographic hash functions (Message Digests).
 *
 * @property digestMode [DigestModes] to be used for MD operations.
 */
class Digest(@NotNull val digestMode: DigestModes) : KipherProvider(provider) {

    /**
     * Generate hash from multiple [data].
     *
     * If you want a string representation of the hash, use [generateHashString],
     * or use [toHexString] on the resulting [ByteArray] hash.
     */
    fun generateHash(@NotNull data: Iterable<ByteArray>): ByteArray {
        val md = MessageDigest.getInstance(digestMode.mode)

        for (item in data) {
            md.update(item)
        }

        return md.digest()
    }

    /**
     * Generate hash from [data].
     *
     * If you want a string representation of the hash, use [generateHashString],
     * or use [toHexString] on the resulting [ByteArray] hash.
     */
    fun generateHash(@NotNull data: ByteArray): ByteArray = generateHash(listOf(data))

    /**
     * Generate hash from [data].
     */
    fun generateHashString(@NotNull data: ByteArray): String = generateHash(data).toHexString()

    /**
     * Generate hash from multiple [data].
     */
    fun generateHashString(@NotNull data: Iterable<ByteArray>): String =
        generateHash(data).toHexString()

    /**
     * Verify if [data] matches the [expected] hash.
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull expected: ByteArray,
    ): Boolean = generateHash(data).contentEquals(expected)

    /**
     * Verify if multiple [data] matches the [expected] hash.
     *
     * The list of [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: Iterable<ByteArray>,
        @NotNull expected: ByteArray,
    ): Boolean = generateHash(data).contentEquals(expected)

    /**
     * Verify if [data] matches the [expected] hash.
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull expected: String,
    ): Boolean = generateHash(data).toHexString().contentEquals(expected)

    /**
     * Verify if multiple [data] matches the [expected] hash.
     *
     * The list of [data] to be verified should be in the same order
     * as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: Iterable<ByteArray>,
        @NotNull expected: String,
    ): Boolean = generateHash(data).toHexString().contentEquals(expected)

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null
    }
}
