/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.common

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.security.Provider
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher

/**
 * Base encryption class that provides common methods
 * and properties across kipher modules.
 *
 * @param transformation the name of the transformation.
 * @param provider security provider JCE will use. (defaults to Bouncy Castle)
 */
abstract class BaseEncryption @JvmOverloads constructor(
    @NotNull transformation: String,
    @Nullable provider: Provider? = null,
) {

    init {
        try {
            // add bouncy castle as default security provider
            Security.addProvider(provider ?: BouncyCastleProvider())
        } catch (e: SecurityException) {
            throw KipherException("Error setting up provider", e)
        }

        try {
            Security.setProperty("crypto.policy", "unlimited")
        } catch (e: SecurityException) {
            throw KipherException(
                "Error setting up crypto policy.\n" +
                    "Please make sure you have the unlimited strength policy files installed: " +
                    "https://www.oracle.com/java/technologies/javase-jce-all-downloads.html",
                e,
            )
        }
    }

    /** secure random number generator. */
    val randomize = SecureRandom()

    /**
     * Set cipher transformation mode and provider (if provided).
     *
     * Provider defaults to Bouncy Castle.
     */
    val cipher: Cipher = Cipher.getInstance(transformation, provider ?: BouncyCastleProvider())
}
