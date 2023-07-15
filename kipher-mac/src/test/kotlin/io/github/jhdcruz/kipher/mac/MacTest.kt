/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class MacTest {

    private val dataList: List<ByteArray> = listOf(
        "test".encodeToByteArray(),
        "test2".encodeToByteArray(),
        "test3".encodeToByteArray(),
    )

    @Test
    fun `test MAC from multiple data`() {
        val hmac = Mac(MacModes.HmacSHA256)
        val key = hmac.generateKey()

        val actualBytes = hmac.generateHash(dataList, key)

        assertTrue {
            hmac.verifyHash(dataList, actualBytes, key)
        }
    }

    @Test
    fun `test MAC from multiple data with different order`() {
        val hmac = Mac(MacModes.HmacSHA256)
        val key = hmac.generateKey()

        val unsortedList: List<ByteArray> = listOf(
            "test2".encodeToByteArray(),
            "test3".encodeToByteArray(),
            "test".encodeToByteArray(),
        )

        val actualBytes = hmac.generateHash(dataList, key)

        assertFalse {
            hmac.verifyHash(unsortedList, actualBytes, key)
        }
    }

    @ParameterizedTest
    @EnumSource(MacModes::class)
    fun `test different hash modes`(mode: MacModes) {
        val hmac = Mac(mode)
        val key = hmac.generateKey()

        val actualBytes = hmac.generateHashString(dataList, key)

        // print output and length in json like form
        println("$mode = $actualBytes, length = ${actualBytes.length}")

        assertTrue {
            hmac.verifyHash(dataList, actualBytes, key)
        }
    }
}
