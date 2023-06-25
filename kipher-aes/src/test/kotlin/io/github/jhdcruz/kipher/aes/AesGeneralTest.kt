/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import org.junit.jupiter.api.RepeatedTest
import java.security.Provider
import java.security.Security
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesGeneralTest {

    @Test
    fun `test key generation`() {
        val cbcEncryption = CbcEncryption()
        val key = cbcEncryption.generateKey()

        // 32 = 256-bit key. 16 = 128-bit
        assertEquals(32, key.size)
    }

    @RepeatedTest(5)
    fun `test key randomization`() {
        val cbcEncryption = CbcEncryption()

        val firstKey = cbcEncryption.generateKey()
        val secondKey = cbcEncryption.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @Test
    fun `test different providers`() {
        val provider: Provider = Security.getProvider("SunJCE")
        AesEncryption.Companion.provider = provider

        val cbcEncryption = CbcEncryption()

        assertContains(provider, cbcEncryption.cipher.provider)
    }
}
