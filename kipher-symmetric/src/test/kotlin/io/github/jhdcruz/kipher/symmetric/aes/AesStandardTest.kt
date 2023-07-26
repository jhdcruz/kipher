/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.invalidKey
import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.message
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.security.InvalidAlgorithmParameterException
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AesStandardTest {

    @Test
    fun `test basic encryption`() {
        val aesCbc = AesCbc()

        val encrypted = aesCbc.encrypt(message)
        val decrypted = aesCbc.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test basic encryption with invalid secret key`() {
        val aesCbc = AesCbc()

        assertThrows<InvalidAlgorithmParameterException> {
            aesCbc.encrypt(message, invalidKey)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [128, 192, 256])
    fun `test basic encryption with valid custom key size`(keySize: Int) {
        val aesCbc = AesCbc()

        val secretKey = aesCbc.generateKey(keySize)
        val encrypted = aesCbc.encrypt(message, secretKey)

        val decrypted = aesCbc.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
        )

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 69, 100, 500])
    fun `test encryption with invalid custom key size`(keySize: Int) {
        val aesCbc = AesCbc()
        val secretKey = aesCbc.generateKey(keySize)

        assertThrows<InvalidAlgorithmParameterException> {
            aesCbc.encrypt(message, secretKey)
        }
    }
}
