/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.aes.AesParams.aad
import io.github.jhdcruz.kipher.aes.AesParams.decodeToString
import io.github.jhdcruz.kipher.aes.AesParams.invalidKey
import io.github.jhdcruz.kipher.aes.AesParams.message
import io.github.jhdcruz.kipher.common.KipherException
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AesAuthenticatedTest {

    @Test
    fun `test authenticated encryption`() {
        val gcmEncryption = GcmEncryption()

        val encrypted = gcmEncryption.encrypt(
            data = message,
            aad = aad,
        )

        val decrypted = gcmEncryption.decrypt(encrypted)

        assertEquals(
            decodeToString(message),
            decodeToString(decrypted),
        )
    }

    @Test
    fun `test authenticated encryption without AAD`() {
        val gcmEncryption = GcmEncryption()

        val encrypted = gcmEncryption.encrypt(message)

        val decrypted = gcmEncryption.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
        )

        assertEquals(
            decodeToString(message),
            decodeToString(decrypted),
        )
    }

    @Test
    fun `test authenticated encryption with wrong metadata`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey()
        val encrypted = gcmEncryption.encryptBare(
            data = message,
            iv = gcmEncryption.generateIv(),
            key = secretKey,
            aad = aad,
        )

        assertThrows<KipherException> {
            gcmEncryption.decryptBare(
                encrypted["data"]!!,
                encrypted["iv"]!!,
                secretKey,
                "invalid-aad".encodeToByteArray(),
            )
        }
    }

    @Test
    fun `test authenticated encryption using invalid secret key`() {
        val gcmEncryption = GcmEncryption()

        assertThrows<KipherException> {
            gcmEncryption.encrypt(message, invalidKey, aad)
        }
    }

    @Test
    fun `test authenticated encryption with same AAD as aadSeparator`() {
        val gcmEncryption = GcmEncryption()

        val aad = gcmEncryption.aadSeparator

        val encrypted = gcmEncryption.encrypt(
            data = message,
            aad = aad,
        )

        assertDoesNotThrow {
            gcmEncryption.decrypt(
                encrypted = encrypted.getValue("data"),
                key = encrypted.getValue("key"),
            )
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [128, 192, 256])
    fun `test authenticated encryption with valid custom key size`(keySize: Int) {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey(keySize)
        val encrypted = gcmEncryption.encrypt(message, aad, secretKey)

        val decrypted = gcmEncryption.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 69, 100, 500])
    fun `test authenticated encryption with invalid custom key size`(keySize: Int) {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey(keySize)

        assertThrows<KipherException> {
            gcmEncryption.encrypt(message, secretKey, aad)
        }
    }
}
