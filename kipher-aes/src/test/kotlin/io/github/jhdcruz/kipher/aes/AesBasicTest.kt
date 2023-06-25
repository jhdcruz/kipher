package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.aes.AesParams.decodeToString
import io.github.jhdcruz.kipher.aes.AesParams.invalidKey
import io.github.jhdcruz.kipher.aes.AesParams.message
import io.github.jhdcruz.kipher.common.KipherException
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesBasicTest {

    @Test
    fun `test key generation`() {
        val cbcEncryption = CbcEncryption()

        // no need to test for authenticated since it uses the same function
        val key = cbcEncryption.generateKey()

        // 32 = 256-bit key. 16 = 128-bit
        assertEquals(32, key.size)
    }

    @RepeatedTest(5)
    fun `test key randomization`() {
        val cbcEncryption = CbcEncryption()

        // no need to test for authenticated since it uses the same function
        val firstKey = cbcEncryption.generateKey()
        val secondKey = cbcEncryption.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @Test
    fun `test basic encryption`() {
        val cbcEncryption = CbcEncryption()

        val encrypted = cbcEncryption.encrypt(message)
        val decrypted = cbcEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test basic encryption with invalid secret key`() {
        val cbcEncryption = CbcEncryption()

        assertThrows<KipherException> {
            cbcEncryption.encrypt(message, invalidKey)
        }
    }

    @Test
    fun `test basic encryption with custom key size`() {
        val cbcEncryption = CbcEncryption()

        val secretKey = cbcEncryption.generateKey(128)
        val encrypted = cbcEncryption.encrypt(message, secretKey)

        val decrypted = cbcEncryption.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }
}
