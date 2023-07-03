/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.hash.digest

import io.github.jhdcruz.kipher.hash.HashUtils.toHexString
import kotlin.test.Test
import kotlin.test.assertTrue

internal class DigestModesTest {

    private val data = "test".encodeToByteArray()

    @Test
    fun `test MD5 hash generation`() {
        val digest = DigestMD5()

        val hash = digest.generateHash(data)

        println(hash.toHexString())

        assertTrue {
            digest.verifyHash(data, hash)
        }
    }

    @Test
    fun `test SHA-224 hash generation`() {
        val digest = DigestSHA224()

        val hash = digest.generateHash(data)

        println(hash.toHexString())

        assertTrue {
            digest.verifyHash(data, hash)
        }
    }

    @Test
    fun `test SHA-256 hash generation`() {
        val digest = DigestSHA256()

        val hash = digest.generateHash(data)

        println(hash.toHexString())

        assertTrue {
            digest.verifyHash(data, hash)
        }
    }

    @Test
    fun `test SHA-384 hash generation`() {
        val digest = DigestSHA384()

        val hash = digest.generateHash(data)

        println(hash.toHexString())

        assertTrue {
            digest.verifyHash(data, hash)
        }
    }

    @Test
    fun `test SHA-512 hash generation`() {
        val digest = DigestSHA512()

        val hash = digest.generateHash(data)

        println(hash.toHexString())

        assertTrue {
            digest.verifyHash(data, hash)
        }
    }
}
