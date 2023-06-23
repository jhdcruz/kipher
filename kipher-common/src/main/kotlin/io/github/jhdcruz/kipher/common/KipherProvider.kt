/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.common

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.jetbrains.annotations.Nullable
import java.security.Provider
import java.security.Security

/**
 * Provides common methods and properties across kipher modules.
 *
 * @param provider security provider JCE will use. (defaults to Bouncy Castle)
 */
open class KipherProvider @JvmOverloads constructor(
    @Nullable provider: Provider = BouncyCastleProvider(),
) {

    init {
        try {
            // add bouncy castle as default security provider
            Security.addProvider(provider)
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
}
