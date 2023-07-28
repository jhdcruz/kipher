/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric

import io.github.jhdcruz.kipher.symmetric.SymmetricTestParams.aad
import io.github.jhdcruz.kipher.symmetric.SymmetricTestParams.invalidKey
import io.github.jhdcruz.kipher.symmetric.SymmetricTestParams.message
import io.github.jhdcruz.kipher.symmetric.aes.AesCBC
import io.github.jhdcruz.kipher.symmetric.aes.AesGCM
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import java.security.InvalidAlgorithmParameterException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Test general methods in symmetric encryption.
 */
internal class BaseSymmetricTest {

    @Test
    fun `test standard encryption with invalid secret key`() {
        val aesCbc = AesCBC()

        assertThrows<InvalidAlgorithmParameterException> {
            aesCbc.encrypt(message, invalidKey)
        }
    }

    @Test
    fun `test authenticated encryption using invalid secret key`() {
        val aesGcm = AesGCM()

        assertThrows<InvalidAlgorithmParameterException> {
            aesGcm.encrypt(message, invalidKey, aad)
        }
    }

    @Test
    fun `test standard decryption with invalid secret key`() {
        val aesCbc = AesCBC()
        val encrypted = aesCbc.encrypt(message)

        assertThrows<InvalidAlgorithmParameterException> {
            aesCbc.decrypt(encrypted["data"]!!, invalidKey)
        }
    }

    @Test
    fun `test authenticated decryption using invalid secret key`() {
        val aesGcm = AesGCM()
        val encrypted = aesGcm.encrypt(message, aad)

        assertThrows<InvalidAlgorithmParameterException> {
            aesGcm.decrypt(encrypted["data"]!!, invalidKey, encrypted["aad"]!!)
        }
    }

    @ParameterizedTest
    @CsvSource(
        // key length, bits
        "16, 128",
        "24, 192",
        "32, 256",
    )
    fun `test key generation`(bits: Int, keyLength: Int) {
        val aesCbc = AesCBC()
        val key = aesCbc.generateKey(keyLength)

        assertEquals(bits, key.size)
    }

    @RepeatedTest(5)
    fun `test key randomization`() {
        val aesCbc = AesCBC()

        val firstKey = aesCbc.generateKey()
        val secondKey = aesCbc.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 69, 100, 500])
    fun `test encryption with invalid custom key size`(keySize: Int) {
        val aesCbc = AesCBC()
        val secretKey = aesCbc.generateKey(keySize)

        assertThrows<InvalidAlgorithmParameterException> {
            aesCbc.encrypt(message, secretKey)
        }
    }
}
