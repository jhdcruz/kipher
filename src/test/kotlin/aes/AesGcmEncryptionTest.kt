package aes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AesGcmEncryptionTest {
    @Test
    fun `test key bytes generation`() {
        val aesGcmEncryption = AesGcmEncryption()
        val keyBytes = aesGcmEncryption.generateKey()

        assertEquals(16, keyBytes.size)
    }

    @Test
    fun `test encryption`() {
        val aesGcmEncryption = AesGcmEncryption()
        val secretKey = aesGcmEncryption.generateKey()

        val message = "admin"
        val cipherText = aesGcmEncryption.encrypt(message, secretKey)

        assertEquals(33, cipherText.size)
    }

    @Test
    fun `test decryption`() {
        val aesGcmEncryption = AesGcmEncryption()
        val secretKey = aesGcmEncryption.generateKey()

        val message = "admin"
        val cipherText = aesGcmEncryption.encrypt(message, secretKey)
        val decrypted = aesGcmEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }
}
