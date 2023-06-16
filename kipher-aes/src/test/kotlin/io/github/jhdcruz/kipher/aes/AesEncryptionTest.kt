package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.common.KipherException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

        // no need to test for GCM since it uses the same function
        val key = cbcEncryption.generateKey()

        assertEquals(32, key.size)
    }

    @Test
    fun `test key randomization`() {
        val cbcEncryption = CbcEncryption()

        // no need to test for GCM since it uses the same function
        val firstKey = cbcEncryption.generateKey()
        val secondKey = cbcEncryption.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @Test
    fun `test basic encryption with IV`() {
        val cbcEncryption = CbcEncryption()

        val secretKey = cbcEncryption.generateKey()
        val cipherText: Pair<ByteArray, ByteArray> = cbcEncryption.encryptWithIv(message, secretKey)
        val decrypted = cbcEncryption.decrypt(cipherText.second, secretKey, cipherText.first)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test authenticated encryption with IV`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey()
        val cipherText: Pair<ByteArray, ByteArray> =
            gcmEncryption.encryptWithIv(message, aad, secretKey)
        val decrypted = gcmEncryption.decrypt(cipherText.second, aad, secretKey, cipherText.first)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test authenticated encryption with wrong metadata`() {
        val authenticatedEncryption = AuthenticatedEncryption(AesModes.GCM)

        val secretKey = authenticatedEncryption.generateKey()
        val cipherText = authenticatedEncryption.encrypt(message, aad, secretKey)

        assertThrows<KipherException> {
            authenticatedEncryption.decrypt(
                cipherText,
                "wrong-metadata".encodeToByteArray(),
                secretKey,
            )
        }
    }

    @Test
    fun `test authenticated encryption using invalid secret key`() {
        val authenticatedEncryption = AuthenticatedEncryption(AesModes.GCM)

        assertThrows<KipherException> {
            authenticatedEncryption.encrypt(message, aad, invalidKey)
        }
    }

    @Test
    fun `test basic encryption with invalid secret key`() {
        val cbcEncryption = BasicEncryption(AesModes.CBC)

        assertThrows<KipherException> {
            cbcEncryption.encrypt(message, invalidKey)
        }
    }

    @Test
    fun `test encryption with custom key size`() {
        val gcmEncryption = GcmEncryption(192)

        val secretKey = gcmEncryption.generateKey()
        val cipherText = gcmEncryption.encrypt(message, aad, secretKey)
        val decrypted = gcmEncryption.decrypt(cipherText, aad, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test encryption with invalid custom key size`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey(69)

        assertThrows<KipherException> {
            gcmEncryption.encrypt(message, aad, secretKey)
        }
    }
}
