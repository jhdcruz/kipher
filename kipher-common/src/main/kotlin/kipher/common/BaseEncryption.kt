/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.common

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher

abstract class BaseEncryption {
    /** secure random number generator */
    val randomize = SecureRandom()

    init {
        // add bouncy castle as security provider if bouncycastle is true
        Security.addProvider(BouncyCastleProvider())
    }

    /** Set cipher type and modes, including provider */
    abstract val cipher: Cipher
}
