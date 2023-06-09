/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

@file:Suppress("MagicNumber")

package io.github.jhdcruz.kipher.rsa

import io.github.jhdcruz.kipher.common.KipherException
import java.security.GeneralSecurityException
import java.security.InvalidParameterException
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.spec.MGF1ParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.spec.OAEPParameterSpec
import javax.crypto.spec.PSource
import kotlin.jvm.Throws

class KipherRsa @JvmOverloads constructor(
    private val keySize: Int = 3072,
    private val rsaMode: RsaModes = RsaModes.OAEP_256,
) : KipherRsaImpl() {

    /** Cipher instance for RSA encryption. */
    private val cipher: Cipher = Cipher.getInstance(
        rsaMode.mode,
        // use bouncy castle as provider if set to true
        "BC",
    )

    private val paramSpec = OAEPParameterSpec(
        when (rsaMode) {
            RsaModes.OAEP_512 -> "SHA-512"
            RsaModes.OAEP_256 -> "SHA-256"
        },
        "MGF1",
        MGF1ParameterSpec.SHA256,
        PSource.PSpecified.DEFAULT,
    )

    /** Generate a keypair as [ByteArray]. */
    @Throws(KipherException::class)
    override fun generateKeyPair(): KeyPair {
        return try {
            val keyGen = KeyPairGenerator.getInstance("RSA").apply {
                initialize(
                    when (rsaMode) {
                        RsaModes.OAEP_256 -> 3072
                        RsaModes.OAEP_512 -> 15360
                        else -> keySize
                    },
                )
            }

            keyGen.generateKeyPair()
        } catch (e: InvalidParameterException) {
            throw KipherException(e)
        }
    }

    /** Encrypts the provided [data] using the provided [publicKey]. */
    @Throws(KipherException::class)
    override fun encrypt(data: ByteArray, publicKey: ByteArray): ByteArray {
        return try {
            // parse the given public key
            val publicKeySpec = X509EncodedKeySpec(publicKey)
            val parsedKey = keyFactory.generatePublic(publicKeySpec)

            cipher.init(Cipher.ENCRYPT_MODE, parsedKey, paramSpec)

            cipher.doFinal(data)
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }

    /** Decrypts [encrypted] data using [privateKey]. */
    @Throws(KipherException::class)
    override fun decrypt(encrypted: ByteArray, privateKey: ByteArray): ByteArray {
        return try {
            // parse the given private key
            val privateKeySpec = X509EncodedKeySpec(privateKey)
            val parsedKey = keyFactory.generatePrivate(privateKeySpec)

            cipher.init(Cipher.DECRYPT_MODE, parsedKey, paramSpec)

            cipher.doFinal(encrypted)
        } catch (e: GeneralSecurityException) {
            throw KipherException(e)
        }
    }
}
