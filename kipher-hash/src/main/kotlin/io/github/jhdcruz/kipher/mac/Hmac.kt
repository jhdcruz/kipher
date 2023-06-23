/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

import io.github.jhdcruz.kipher.common.KipherException
import io.github.jhdcruz.kipher.common.KipherProvider
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jetbrains.annotations.NotNull
import java.security.InvalidKeyException
import java.security.Provider
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Data authenticity and integrity checks using
 * Keyed hash-based message authentication code (HMACs).
 *
 * @property macMode [MacModes] to be used for HMAC operations.
 */
class Hmac(@NotNull val macMode: MacModes) : KipherProvider(provider) {
    private val mode = macMode.mode

    /**
     *  Generate HMAC for [data] using provided [key].
     */
    @Throws(KipherException::class)
    fun generateHmac(
        @NotNull data: ByteArray,
        @NotNull key: ByteArray
    ): ByteArray {
        return try {
            val hmac = Mac.getInstance(mode)
            val secretKey = SecretKeySpec(key, mode)

            hmac.init(secretKey)

            hmac.doFinal(data)
        } catch (e: InvalidKeyException) {
            throw KipherException("inappropriate key for initializing MAC", e)
        } catch (e: IllegalStateException) {
            throw KipherException(e)
        }
    }

    /**
     * Verify [hmac] from [data] using provided [key].
     */
    fun verifyHmac(data: ByteArray, hmac: ByteArray, key: ByteArray): Boolean {
        return hmac.contentEquals(
            generateHmac(data, key)
        )
    }

    /**
     * Verify [hmac] from [data] using provided [key].
     */
    fun verifyHmac(data: ByteArray, hmac: String, key: ByteArray): Boolean {
        return hmac.contentEquals(
            String(generateHmac(data, key))
        )
    }

    companion object {
        /** Set JCE security provider. */
        var provider: Provider = BouncyCastleProvider()
    }
}
