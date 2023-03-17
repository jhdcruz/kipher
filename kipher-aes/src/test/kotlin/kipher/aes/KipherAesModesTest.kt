package kipher.aes

import org.junit.jupiter.api.Test
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
    fun `test class singular constructors`() {
        KipherAes(256)
        KipherAes(AesModes.CBC)
    }
}
