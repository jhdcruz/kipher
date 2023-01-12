package kipher.aes

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class AesEncryptionTest {
    private val message = "message"
    private val metadata = "metadata".encodeToByteArray()
    private val invalidKey = "invalid-key".encodeToByteArray()

    @Test
    fun `test key generation`() {
        val aesEncryption = AesEncryption()
        val key = aesEncryption.generateKey()

        assertEquals(32, key.size)
    }

    @Test
    fun `test key randomization`() {
        val aesEncryption = AesEncryption()

        val firstKey = aesEncryption.generateKey()
        val secondKey = aesEncryption.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @Test
    fun `test encryption`() {
        val aesEncryption = AesEncryption()

        val secretKey = aesEncryption.generateKey()
        val cipherText = aesEncryption.encrypt(message, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with metadata`() {
        val aesEncryption = AesEncryption()

        val secretKey = aesEncryption.generateKey()
        val cipherText = aesEncryption.encrypt(message, metadata, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, metadata, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with wrong metadata`() {
        val aesEncryption = AesEncryption()

        val secretKey = aesEncryption.generateKey()
        val cipherText = aesEncryption.encrypt(message, metadata, secretKey)

        assertThrows<AesEncryptionException> {
            aesEncryption.decrypt(cipherText, "wrong-metadata".encodeToByteArray(), secretKey)
        }
    }

    @Test
    fun `test encryption with metadata using invalid secret key`() {
        val aesEncryption = AesEncryption()

        assertThrows<AesEncryptionException> {
            aesEncryption.encrypt(message, metadata, invalidKey)
        }
    }

    @Test
    fun `test encryption with invalid secret key`() {
        val aesEncryption = AesEncryption()

        assertThrows<AesEncryptionException> {
            aesEncryption.encrypt(message, invalidKey)
        }
    }

    @Test
    fun `test encryption with custom key size`() {
        val aesEncryption = AesEncryption(192)

        val secretKey = aesEncryption.generateKey()
        val cipherText = aesEncryption.encrypt(message, metadata, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, metadata, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption with invalid custom key size`() {
        assertThrows<AesEncryptionException> {
            AesEncryption(123).generateKey()
        }
    }

    @Test
    fun `test decryption with invalid secret key`() {
        val aesEncryption = AesEncryption()

        val secretKey = aesEncryption.generateKey()
        val cipherText = aesEncryption.encrypt(message, secretKey)

        assertThrows<AesEncryptionException> {
            aesEncryption.decrypt(cipherText, invalidKey)
        }
    }
}
