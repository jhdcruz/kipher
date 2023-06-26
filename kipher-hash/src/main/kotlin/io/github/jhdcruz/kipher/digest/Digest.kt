/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.digest

import io.github.jhdcruz.kipher.common.KipherProvider
import org.jetbrains.annotations.NotNull
import java.security.MessageDigest
import java.security.Provider
import java.util.Base64

/**
 * Data authenticity and integrity checks using
 * Cryptographic hash functions (Message Digests).
 *
 * @property digestMode [DigestModes] to be used for MD operations.
 */
sealed class Digest(@NotNull val digestMode: DigestModes) : KipherProvider(provider) {
    private val mode = digestMode.mode

    /**
     * Generate hash from multiple [data].
     */
    fun generateHash(@NotNull data: List<ByteArray>): ByteArray {
        val md = MessageDigest.getInstance(mode)

        for (item in data) {
            md.update(item)
        }

        return md.digest()
    }

    /**
     * Generate hash from [data].
     */
    fun generateHash(@NotNull data: ByteArray): ByteArray = generateHash(listOf(data))

    /**
     * Generate hash from [data].
     */
    fun generateHashString(@NotNull data: ByteArray): String = generateHash(data).hashString()

    /**
     * Generate hash from multiple [data].
     */
    fun generateHashString(@NotNull data: List<ByteArray>): String = generateHash(data).hashString()

    /**
     *  Verify if [hash] matches with [data].
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull hash: ByteArray,
    ): Boolean = hash.contentEquals(generateHash(data))

    /**
     *  Verify if [hash] matches with multiple [data].
     *
     *  [data] to be verified should be in the same order
     *  as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: List<ByteArray>,
        @NotNull hash: ByteArray,
    ): Boolean = hash.contentEquals(generateHash(data))

    /**
     *  Verify if [hash] matches with [data].
     */
    fun verifyHash(
        @NotNull data: ByteArray,
        @NotNull hash: String,
    ): Boolean = hash.contentEquals(generateHash(data).hashString())

    /**
     *  Verify if [hash] matches with multiple [data].
     *
     *  [data] to be verified should be in the same order
     *  as the original data when the hash was generated.
     */
    fun verifyHash(
        @NotNull data: List<ByteArray>,
        @NotNull hash: String,
    ): Boolean = hash.contentEquals(generateHash(data).hashString())

    companion object {
        /** Set JCE security provider. */
        var provider: Provider? = null

        /** Convert [ByteArray] hash to [String]. */
        fun ByteArray.hashString(): String = Base64.getEncoder().encodeToString(this)
    }
}
