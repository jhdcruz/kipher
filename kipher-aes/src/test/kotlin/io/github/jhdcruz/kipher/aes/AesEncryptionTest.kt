package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherException
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesEncryptionTest {
    private val message = "message".encodeToByteArray()
    private val aad = "metadata".encodeToByteArray()
    private val invalidKey = "invalid-key".encodeToByteArray()

    private val decodeToString = { bytes: ByteArray -> String(bytes, Charsets.UTF_8) }

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
    fun `test authenticated encryption`() {
        val gcmEncryption = GcmEncryption()

        val encrypted = gcmEncryption.encrypt(
            data = message,
            aad = aad
        )

        val decrypted = gcmEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test authenticated encryption without AAD`() {
        val gcmEncryption = GcmEncryption()

        val encrypted = gcmEncryption.encrypt(message)

        val decrypted = gcmEncryption.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test authenticated encryption with wrong metadata`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey()
        val encrypted = gcmEncryption.encryptBare(
            data = message,
            iv = gcmEncryption.generateIv(),
            key = secretKey,
            aad = aad
        )

        assertThrows<KipherException> {
            gcmEncryption.decryptBare(
                encrypted["data"]!!,
                encrypted["iv"]!!,
                secretKey,
                "invalid-aad".encodeToByteArray()
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
    fun `test authenticated encryption with same AAD as separator`() {
        val gcmEncryption = GcmEncryption()

        // gcmEncryption companion object
        val aad = AuthenticatedEncryption.aadSeparator

        val encrypted = gcmEncryption.encrypt(
            data = message,
            aad = aad
        )

        assertDoesNotThrow {
            gcmEncryption.decrypt(
                encrypted = encrypted.getValue("data"),
                key = encrypted.getValue("key")
            )
        }
    }

    @Test
    fun `test basic encryption with invalid secret key`() {
        val cbcEncryption = CbcEncryption()

        assertThrows<KipherException> {
            cbcEncryption.encrypt(message, invalidKey)
        }
    }

    @Test
    fun `test encryption with custom key size`() {
        val cbcEncryption = CbcEncryption()

        val secretKey = cbcEncryption.generateKey(128)
        val encrypted = cbcEncryption.encrypt(message, secretKey)

        val decrypted = cbcEncryption.decrypt(
            encrypted = encrypted["data"]!!,
            key = encrypted["key"]!!
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @ParameterizedTest
    @ValueSource(ints = [1, 69, 100, 125, 500])
    fun `test encryption with invalid custom key size`(keySize: Int) {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey(keySize)

        assertThrows<KipherException> {
            gcmEncryption.encrypt(message, secretKey, aad)
        }
    }
}
