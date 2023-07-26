/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

import io.github.jhdcruz.kipher.core.KipherException
import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.aad
import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.invalidKey
import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.message
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.security.InvalidAlgorithmParameterException
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AesAuthenticatedTest {

    @Test
    fun `test authenticated encryption`() {
        val aesGcm = AesGcm()

        val encrypted = aesGcm.encrypt(
            data = message,
            aad = aad,
        )

        val decrypted = aesGcm.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test authenticated encryption without AAD`() {
        val aesGcm = AesGcm()

        val encrypted = aesGcm.encrypt(message)

        val decrypted = aesGcm.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
        )

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test authenticated encryption with wrong metadata`() {
        val aesGcm = AesGcm()

        val secretKey = aesGcm.generateKey()
        val encrypted = aesGcm.encryptBare(
            data = message,
            iv = aesGcm.generateIv(),
            key = secretKey,
            aad = aad,
        )

        assertThrows<KipherException> {
            aesGcm.decryptBare(
                encrypted["data"]!!,
                encrypted["iv"]!!,
                secretKey,
                "invalid-aad".encodeToByteArray(),
            )
        }
    }

    @Test
    fun `test authenticated encryption using invalid secret key`() {
        val aesGcm = AesGcm()

        assertThrows<InvalidAlgorithmParameterException> {
            aesGcm.encrypt(message, invalidKey, aad)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [128, 192, 256])
    fun `test authenticated encryption with valid custom key size`(keySize: Int) {
        val aesGcm = AesGcm()

        val secretKey = aesGcm.generateKey(keySize)
        val encrypted = aesGcm.encrypt(message, aad, secretKey)

        val decrypted = aesGcm.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
            aad = encrypted["aad"]!!,
        )

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 69, 100, 500])
    fun `test authenticated encryption with invalid custom key size`(keySize: Int) {
        val aesGcm = AesGcm()

        val secretKey = aesGcm.generateKey(keySize)

        assertThrows<InvalidAlgorithmParameterException> {
            aesGcm.encrypt(message, secretKey, aad)
        }
    }
}
