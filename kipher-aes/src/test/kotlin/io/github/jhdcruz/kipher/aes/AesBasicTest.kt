package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.aes.AesTestParams.decodeToString
import io.github.jhdcruz.kipher.aes.AesTestParams.invalidKey
import io.github.jhdcruz.kipher.aes.AesTestParams.message
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.security.InvalidAlgorithmParameterException
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AesBasicTest {

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

        assertThrows<InvalidAlgorithmParameterException> {
            cbcEncryption.encrypt(message, invalidKey)
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [128, 192, 256])
    fun `test basic encryption with valid custom key size`(keySize: Int) {
        val cbcEncryption = CbcEncryption()

        val secretKey = cbcEncryption.generateKey(keySize)
        val encrypted = cbcEncryption.encrypt(message, secretKey)

        val decrypted = cbcEncryption.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!,
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 69, 100, 500])
    fun `test encryption with invalid custom key size`(keySize: Int) {
        val cbcEncryption = CbcEncryption()
        val secretKey = cbcEncryption.generateKey(keySize)

        assertThrows<InvalidAlgorithmParameterException> {
            cbcEncryption.encrypt(message, secretKey)
        }
    }
}
