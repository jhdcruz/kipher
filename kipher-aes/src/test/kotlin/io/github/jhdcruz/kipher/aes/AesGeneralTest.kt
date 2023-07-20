/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesGeneralTest {
    @ParameterizedTest
    @CsvSource(
        // key length, bits
        "16, 128",
        "24, 192",
        "32, 256",
    )
    fun `test key generation`(bits: Int, keyLength: Int) {
        val cbcEncryption = CbcEncryption()
        val key = cbcEncryption.generateKey(keyLength)

        assertEquals(bits, key.size)
    }

    @RepeatedTest(5)
    fun `test key randomization`() {
        val cbcEncryption = CbcEncryption()

        val firstKey = cbcEncryption.generateKey()
        val secondKey = cbcEncryption.generateKey()

        assertNotEquals(firstKey, secondKey)
    }
}
