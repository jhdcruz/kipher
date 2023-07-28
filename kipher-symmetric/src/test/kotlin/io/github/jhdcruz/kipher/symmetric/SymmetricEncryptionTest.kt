/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric

import io.github.jhdcruz.kipher.core.Format.toHexString
import io.github.jhdcruz.kipher.symmetric.SymmetricTestParams.aad
import io.github.jhdcruz.kipher.symmetric.SymmetricTestParams.message
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.test.assertEquals

/**
 * Consolidated symmetric encryption test.
 *
 * Classes are consolidated in [Stream] and tested,
 * this brings a certain benefit that all classes should behave similarly,
 * and some methods should exists in all classes and are tested per class.
 */
internal class SymmetricEncryptionTest {

    @ParameterizedTest
    @MethodSource("io.github.jhdcruz.kipher.symmetric.SymmetricTestParams#getStandardClasses")
    fun `Standard encryption test`(encryptionClass: Class<out StandardEncryption>) {
        val encryption = encryptionClass.getDeclaredConstructor().newInstance()

        val encrypted = encryption.encrypt(message)
        val decrypted = encryption.decrypt(encrypted)

        println("${encryption.mode} = ${encrypted["data"]!!.size}")
        println("key size = ${encrypted["key"]!!.size * 8}")
        println("------------------------------------------------------------------------")

        assertEquals(message.decodeToString(), decrypted.decodeToString())
    }

    @ParameterizedTest
    @MethodSource("io.github.jhdcruz.kipher.symmetric.SymmetricTestParams#getStandardClasses")
    fun `Standard encryption test with parameters`(encryptionClass: Class<out StandardEncryption>) {
        val encryption = encryptionClass.getDeclaredConstructor().newInstance()

        val encrypted = encryption.encrypt(message)
        val decrypted = encryption.decrypt(encrypted["data"]!!, encrypted["key"]!!)

        assertEquals(message.decodeToString(), decrypted.decodeToString())
    }

    @ParameterizedTest
    @MethodSource("io.github.jhdcruz.kipher.symmetric.SymmetricTestParams#getAeadClasses")
    fun `AEAD encryption test`(encryptionClass: Class<out AEAD>) {
        val encryption = encryptionClass.getDeclaredConstructor().newInstance()

        val encrypted = encryption.encrypt(message)
        val decrypted = encryption.decrypt(encrypted)

        println("${encryption.mode} = ${encrypted["data"]!!.size}")
        println("key size = ${encrypted["key"]!!.size * 8}")
        println("------------------------------------------------------------------------")

        assertEquals(message.decodeToString(), decrypted.decodeToString())
    }

    @ParameterizedTest
    @MethodSource("io.github.jhdcruz.kipher.symmetric.SymmetricTestParams#getAeadClasses")
    fun `AEAD encryption test with AAD`(encryptionClass: Class<out AEAD>) {
        val encryption = encryptionClass.getDeclaredConstructor().newInstance()

        val encrypted = encryption.encrypt(message, aad)
        val decrypted = encryption.decrypt(encrypted)

        println("${encryption.mode} = ${encrypted["data"]!!.size}")
        println("key size = ${encrypted["key"]!!.size * 8}")
        println("data = ${encrypted["data"]!!.toHexString()}")
        println("------------------------------------------------------------------------")

        assertEquals(message.decodeToString(), decrypted.decodeToString())
    }

    @ParameterizedTest
    @MethodSource("io.github.jhdcruz.kipher.symmetric.SymmetricTestParams#getAeadClasses")
    fun `AEAD encryption test with parameters`(encryptionClass: Class<out AEAD>) {
        val encryption = encryptionClass.getDeclaredConstructor().newInstance()

        val encrypted = encryption.encrypt(message, aad)
        val decrypted =
            encryption.decrypt(encrypted["data"]!!, encrypted["key"]!!, encrypted["aad"]!!)

        assertEquals(message.decodeToString(), decrypted.decodeToString())
    }
}
