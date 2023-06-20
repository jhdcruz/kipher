/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.common

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher

/**
 * Base encryption class that provides common methods
 * and properties across kipher modules.
 *
 * Sets up [BouncyCastleProvider].
 */
abstract class BaseEncryption {

    init {
        // add bouncy castle as security provider
        Security.addProvider(BouncyCastleProvider())
    }

    /** secure random number generator. */
    val randomize = SecureRandom()

    /** Set cipher type and modes, including provider. */
    abstract val cipher: Cipher
}
