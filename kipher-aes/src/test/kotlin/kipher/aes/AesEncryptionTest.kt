package kipher.aes

import kipher.common.KipherException
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
    fun `test IV generation of GCM`() {
        val gcmEncryption = GcmEncryption()
        val iv = gcmEncryption.generateIv()

        assertEquals(12, iv.size)
    }

    @Test
    fun `test IV generation of CBC`() {
        val cbcEncryption = CbcEncryption()
        val iv = cbcEncryption.generateIv()

        assertEquals(16, iv.size)
    }

    @Test
    fun `test CBC encryption`() {
        val cbcEncryption = CbcEncryption()

        val secretKey = cbcEncryption.generateKey()
        val cipherText = cbcEncryption.encrypt(message, secretKey)
        val decrypted = cbcEncryption.decrypt(cipherText, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test GCM encryption`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey()
        val cipherText = gcmEncryption.encrypt(message, aad, secretKey)
        val decrypted = gcmEncryption.decrypt(cipherText, aad, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test encryption with wrong metadata`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey()
        val cipherText = gcmEncryption.encrypt(message, aad, secretKey)

        assertThrows<KipherException> {
            gcmEncryption.decrypt(cipherText, "wrong-metadata".encodeToByteArray(), secretKey)
        }
    }

    @Test
    fun `test GCM encryption using invalid secret key`() {
        val gcmEncryption = GcmEncryption()

        assertThrows<KipherException> {
            gcmEncryption.encrypt(message, aad, invalidKey)
        }
    }

    @Test
    fun `test CBC encryption with invalid secret key`() {
        val cbcEncryption = CbcEncryption()

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
        val gcmEncryption = GcmEncryption(999)

        val secretKey = gcmEncryption.generateKey()

        assertThrows<KipherException> {
            gcmEncryption.encrypt(message, aad, secretKey)
        }
    }
}
