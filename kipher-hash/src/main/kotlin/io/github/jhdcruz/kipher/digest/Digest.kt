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
     *  Verify if [hash] matches with [data].
     */
    fun verifyHash(@NotNull hash: ByteArray, data: ByteArray): Boolean {
        return hash.contentEquals(
            generateHash(data)
        )
    }

    /**
     *  Verify if [hash] matches with [data].
     */
    fun verifyHash(@NotNull hash: String, data: ByteArray): Boolean {
        return hash.contentEquals(
            String(generateHash(data))
        )
    }

    companion object {
        var provider: Provider = BouncyCastleProvider()
    }
}
