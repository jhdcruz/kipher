/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

import io.github.jhdcruz.kipher.common.Format.toHexString
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ChaCha20Test {
    private val data = "test".encodeToByteArray()
    private val aad = "aad".encodeToByteArray()

    @Test
    fun `ChaCha20 encryption test`() {
        val chaCha20 = ChaEncryption()

        val encrypted = chaCha20.encrypt(data)
        val decrypted = chaCha20.decrypt(encrypted)

        println("data = ${encrypted["data"]!!.toHexString()}")

        assertEquals(data.decodeToString(), decrypted.decodeToString())
    }

    @Test
    fun `ChaCha20-Poly1305 encryption test`() {
        val chaCha20 = ChaPolyEncryption()

        val encrypted = chaCha20.encrypt(data, aad)
        val decrypted = chaCha20.decrypt(encrypted)

        println("data = ${encrypted["data"]!!.toHexString()}")

        assertEquals(data.decodeToString(), decrypted.decodeToString())
    }
}
