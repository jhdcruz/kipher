package kipher.aes

import kipher.common.KipherException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

internal class KipherAesModesTest {
    private val message = "test"

    @Test
    fun `test encryption using CBC`() {
        val kipherAes = KipherAes(256, AesModes.CBC)
        val secretKey = kipherAes.generateKey()

        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using CTR`() {
        val kipherAes = KipherAes(256, AesModes.CTR)
        val secretKey = kipherAes.generateKey()

        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using CFB`() {
        val kipherAes = KipherAes(256, AesModes.CFB)
        val secretKey = kipherAes.generateKey()

        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using OFB`() {
        val kipherAes = KipherAes(256, AesModes.OFB)
        val secretKey = kipherAes.generateKey()

        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertEquals(message, String(decrypted, Charsets.UTF_8))
    }

    @Test
    fun `test encryption using mode that does not support metadata`() {
        val kipherAes = KipherAes(256, AesModes.OFB)
        val secretKey = kipherAes.generateKey()

        assertThrows<KipherException> {
            kipherAes.encrypt(message, "metadata".encodeToByteArray(), secretKey)
        }
    }

    @Test
    fun `test decryption using mode that does not support metadata`() {
        val kipherAes = KipherAes(256, AesModes.OFB)
        val secretKey = kipherAes.generateKey()

        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertThrows<KipherException> {
            kipherAes.decrypt(decrypted, "metadata".encodeToByteArray(), secretKey)
        }
    }

    @Test
    fun `test class singular constructors`() {
        KipherAes(256)
        KipherAes(AesModes.CBC)
    }
}
