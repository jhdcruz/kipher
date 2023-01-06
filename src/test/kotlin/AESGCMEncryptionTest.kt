import kotlin.test.assertEquals
import org.junit.jupiter.api.Test

internal class AESGCMEncryptionTest {
    @Test
    fun `test key bytes generation`() {
        val aesgcmEncryption = AESGCMEncryption()
        val keyBytes = aesgcmEncryption.generateKeyBytes()

        assertEquals(16, keyBytes.size)
    }

    @Test
    fun `test encryption`() {
        val aesgcmEncryption = AESGCMEncryption()
        val secretKey = aesgcmEncryption.generateKeyBytes()

        val message = "admin"
        val cipherText = aesgcmEncryption.encrypt(message, secretKey)

        assertEquals(33, cipherText.size)
    }

    @Test
    fun `test decryption`() {
        val aesgcmEncryption = AESGCMEncryption()
        val secretKey = aesgcmEncryption.generateKeyBytes()

        val message = "admin"
        val cipherText = aesgcmEncryption.encrypt(message, secretKey)
        val decrypted = aesgcmEncryption.decrypt(cipherText, secretKey)

        assertEquals(message, decrypted)
    }
}
