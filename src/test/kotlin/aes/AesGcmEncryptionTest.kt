package aes

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesGcmEncryptionTest {
    @Test
    fun `test key generation`() {
        val aesGcmEncryption = AesGcmEncryption()
        val key = aesGcmEncryption.generateKey()

        assertEquals(16, key.size)
    }

    @Test
    fun `test key randomization`() {
        val aesGcmEncryption = AesGcmEncryption()
        val firstKey = aesGcmEncryption.generateKey()
        val secondKey = aesGcmEncryption.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @Test
    fun `test encryption`() {
        val aesGcmEncryption = AesGcmEncryption()
        val secretKey = aesGcmEncryption.generateKey()

        val message = "admin"
        val cipherText = aesGcmEncryption.encrypt(message, secretKey)
        val decrypted = aesGcmEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with metadata`() {
        val aesGcmEncryption = AesGcmEncryption()
        val secretKey = aesGcmEncryption.generateKey()

        val message = "admin"
        val metadata = "metadata".encodeToByteArray()
        val cipherText = aesGcmEncryption.encrypt(message, metadata, secretKey)
        val decrypted = aesGcmEncryption.decrypt(cipherText, metadata, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test invalid secret key on encryption`() {
        val aesGcmEncryption = AesGcmEncryption()
        val message = "admin"

        assertThrows<RuntimeException> {
            aesGcmEncryption.encrypt(message, "invalid-secret-key".encodeToByteArray())
        }

    }

    @Test
    fun `test invalid secret key on decryption`() {
        val aesGcmEncryption = AesGcmEncryption()
        val secretKey = aesGcmEncryption.generateKey()

        val message = "admin"
        val cipherText = aesGcmEncryption.encrypt(message, secretKey)

        assertThrows<RuntimeException> {
            aesGcmEncryption.decrypt(cipherText, "invalid-secret-key".encodeToByteArray())
        }

    }
}
