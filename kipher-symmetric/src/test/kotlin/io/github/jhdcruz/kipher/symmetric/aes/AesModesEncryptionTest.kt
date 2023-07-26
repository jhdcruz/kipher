/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

import io.github.jhdcruz.kipher.core.KipherProvider
import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.aad
import io.github.jhdcruz.kipher.symmetric.EncryptionTestParams.message
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AesModesEncryptionTest : KipherProvider() {

    @Test
    fun `test CBC encryption with PKCS5`() {
        val aesCbc = AesCbc()

        val encrypted = aesCbc.encrypt(message)
        val decrypted = aesCbc.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test CBC encryption with PKCS7`() {
        val cbcEncryption = AesCbc7()

        val encrypted = cbcEncryption.encrypt(message)
        val decrypted = cbcEncryption.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test GCM encryption`() {
        val aesGcm = AesGcm()

        val encrypted =
            aesGcm.encrypt(
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
    fun `test CCM encryption`() {
        val aesCcm = AesCcm()

        val encrypted =
            aesCcm.encrypt(
                data = message,
                aad = aad,
            )

        val decrypted = aesCcm.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test CFB encryption`() {
        val aesCfb = AesCfb()

        val encrypted = aesCfb.encrypt(message)
        val decrypted = aesCfb.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }

    @Test
    fun `test CTR encryption`() {
        val aesCtr = AesCtr()

        val encrypted = aesCtr.encrypt(message)
        val decrypted = aesCtr.decrypt(encrypted)

        assertEquals(
            message.decodeToString(),
            decrypted.decodeToString(),
        )
    }
}
