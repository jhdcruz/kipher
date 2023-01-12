package kipher.aes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AesEncryptionModesTest {
    private val message = "test"

    @Test
    fun `test encryption using CBC`() {
        val aesEncryption = AesEncryption(256, AesModes.CBC)
        val secretKey = aesEncryption.generateKey()

        val cipherText = aesEncryption.encrypt(message, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using CTR`() {
        val aesEncryption = AesEncryption(256, AesModes.CTR)
        val secretKey = aesEncryption.generateKey()

        val cipherText = aesEncryption.encrypt(message, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using CFB`() {
        val aesEncryption = AesEncryption(256, AesModes.CFB)
        val secretKey = aesEncryption.generateKey()

        val cipherText = aesEncryption.encrypt(message, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using OFB`() {
        val aesEncryption = AesEncryption(256, AesModes.OFB)
        val secretKey = aesEncryption.generateKey()

        val cipherText = aesEncryption.encrypt(message, secretKey)
        val decrypted = aesEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }
}
