package kipher.aes

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.security.InvalidParameterException
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesGcmEncryptionTest {
    private val message = "message"
    private val metadata = "metadata".encodeToByteArray()

    @Test
    fun `test key generation`() {
        val aesGcmEncryption = AesGcmEncryption()
        val key = aesGcmEncryption.generateKey()

        assertEquals(32, key.size)
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
        val cipherText = aesGcmEncryption.encrypt(message, secretKey)
        val decrypted = aesGcmEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with metadata`() {
        val aesGcmEncryption = AesGcmEncryption()

        val secretKey = aesGcmEncryption.generateKey()
        val cipherText = aesGcmEncryption.encrypt(message, metadata, secretKey)
        val decrypted = aesGcmEncryption.decrypt(cipherText, metadata, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with wrong metadata`() {
        val aesGcmEncryption = AesGcmEncryption()

        val secretKey = aesGcmEncryption.generateKey()
        val cipherText = aesGcmEncryption.encrypt(message, metadata, secretKey)

        assertThrows<AesEncryptionException> {
            aesGcmEncryption.decrypt(cipherText, "wrong-metadata".encodeToByteArray(), secretKey)
        }
    }

    @Test
    fun `test encryption with metadata using invalid secret key`() {
        val aesGcmEncryption = AesGcmEncryption()

        assertThrows<AesEncryptionException> {
            aesGcmEncryption.encrypt(message, metadata, "invalid-secret-key".encodeToByteArray())
        }
    }

    @Test
    fun `test encryption with invalid secret key`() {
        val aesGcmEncryption = AesGcmEncryption()

        assertThrows<AesEncryptionException> {
            aesGcmEncryption.encrypt(message, "invalid-secret-key".encodeToByteArray())
        }
    }

    @Test
    fun `test encryption with custom key size`() {
        val aesGcmEncryption = AesGcmEncryption(192)

        val secretKey = aesGcmEncryption.generateKey()
        val cipherText = aesGcmEncryption.encrypt(message, metadata, secretKey)
        val decrypted = aesGcmEncryption.decrypt(cipherText, metadata, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with invalid custom key size`() {
        assertThrows<InvalidParameterException> {
            AesGcmEncryption(123)
        }
    }

    @Test
    fun `test decryption with invalid secret key`() {
        val aesGcmEncryption = AesGcmEncryption()

        val secretKey = aesGcmEncryption.generateKey()
        val cipherText = aesGcmEncryption.encrypt(message, secretKey)

        assertThrows<AesEncryptionException> {
            aesGcmEncryption.decrypt(cipherText, "invalid-secret-key".encodeToByteArray())
        }
    }
}
