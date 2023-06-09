/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

@file:JvmSynthetic

package io.github.jhdcruz.kipher.rsa

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.KeyFactory
import java.security.KeyPair
import java.security.Security

abstract class KipherRsaImpl {
    init {
        Security.addProvider(BouncyCastleProvider())
    }

    internal val keyFactory = KeyFactory.getInstance("RSA")

    /** Generate a keypair as [ByteArray]. */
    abstract fun generateKeyPair(): KeyPair

    /** Encrypts the provided [data] using the provided [publicKey]. */
    abstract fun encrypt(data: ByteArray, publicKey: ByteArray): ByteArray

    /** Decrypts [encrypted] data using [privateKey]. */
    abstract fun decrypt(encrypted: ByteArray, privateKey: ByteArray): ByteArray
}
