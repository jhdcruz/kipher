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
    val randomize = SecureRandom()

    init {
        // add bouncy castle as security provider if bouncycastle is true
        Security.addProvider(BouncyCastleProvider())
    }

    abstract val cipher: Cipher
}
