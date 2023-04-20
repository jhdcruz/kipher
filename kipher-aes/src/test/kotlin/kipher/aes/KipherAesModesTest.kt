package kipher.aes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class KipherAesModesTest {
    private val message = "test".encodeToByteArray()

    private val decodeToString = { bytes: ByteArray -> String(bytes, Charsets.UTF_8) }

    @Test
    fun `test encryption using CBC`() {
        val kipherAes = KipherAes(192, AesModes.CBC)
        val secretKey = kipherAes.generateKey()

        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test class singular constructors`() {
        KipherAes(256)
        KipherAes(AesModes.CBC)
    }
}
