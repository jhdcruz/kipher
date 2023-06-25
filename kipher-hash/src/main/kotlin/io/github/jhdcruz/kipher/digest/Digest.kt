/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.digest

import io.github.jhdcruz.kipher.common.KipherProvider
import org.bouncycastle.jce.provider.BouncyCastleProvider
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
class Digest(@NotNull val digestMode: DigestModes) : KipherProvider(provider) {
    private val mode = digestMode.mode

    /**
     * Generate hash from [data].
     */
    fun generateHash(@NotNull data: ByteArray): ByteArray {
        val md = MessageDigest.getInstance(mode, provider)

        return md.digest(data)
    }

    /**
     * Generate hash from [data].
     */
    fun generateHashString(@NotNull data: ByteArray): String {
        val md = MessageDigest.getInstance(mode, provider)

        return md.digest(data).hashString()
    }

    /**
     *  Verify if [hash] matches with [data].
     */
    fun verifyHash(
        @NotNull hash: ByteArray,
        @NotNull data: ByteArray
    ): Boolean {
        return hash.contentEquals(
            generateHash(data)
        )
    }

    /**
     *  Verify if [hash] matches with [data].
     */
    fun verifyHash(
        @NotNull hash: String,
        @NotNull data: ByteArray
    ): Boolean {
        return hash.contentEquals(
            generateHash(data).hashString()
        )
    }

    companion object {
        /** Set JCE security provider. */
        var provider: Provider = BouncyCastleProvider()

        /**
         * Convert [ByteArray] hash to [String].
         */
        fun ByteArray.hashString(): String {
            return Base64.getEncoder().encodeToString(this)
        }
    }
}
