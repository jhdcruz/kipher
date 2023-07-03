/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.digest

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class DigestGeneralTest {

    private val dataList: List<ByteArray> = listOf(
        "test".encodeToByteArray(),
        "test2".encodeToByteArray(),
        "test3".encodeToByteArray(),
    )

    @Test
    fun `test hashing from multiple data`() {
        val digest = DigestSHA256()

        val actualBytes = digest.generateHash(dataList)

        assertTrue {
            digest.verifyHash(dataList, actualBytes)
        }
    }

    @Test
    fun `test hashing from multiple data with different order`() {
        val digest = DigestSHA256()

        val unsortedList: List<ByteArray> = listOf(
            "test2".encodeToByteArray(),
            "test3".encodeToByteArray(),
            "test".encodeToByteArray(),
        )

        val actualBytes = digest.generateHash(dataList)

        assertFalse {
            digest.verifyHash(unsortedList, actualBytes)
        }
    }
}
