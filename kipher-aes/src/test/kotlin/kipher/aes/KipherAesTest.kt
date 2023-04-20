package kipher.aes

import kipher.common.KipherException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class KipherAesTest {
    private val message = "message".encodeToByteArray()
    private val aad = "metadata".encodeToByteArray()
    private val invalidKey = "invalid-key".encodeToByteArray()

    private val decodeToString = { bytes: ByteArray -> String(bytes, Charsets.UTF_8) }

    @Test
    fun `test key generation`() {
        val kipherAes = KipherAes()
        val key = kipherAes.generateKey()

        assertEquals(32, key.size)
    }

    @Test
    fun `test key randomization`() {
        val kipherAes = KipherAes()

        val firstKey = kipherAes.generateKey()
        val secondKey = kipherAes.generateKey()

        assertNotEquals(firstKey, secondKey)
    }

    @Test
    fun `test IV generation of GCM`() {
        val kipherAes = KipherAes()
        val iv = kipherAes.generateIv()

        assertEquals(12, iv.size)
    }

    @Test
    fun `test IV generation of CBC`() {
        val kipherAes = KipherAes(AesModes.CBC)
        val iv = kipherAes.generateIv()

        assertEquals(16, iv.size)
    }

    @Test
    fun `test encryption`() {
        val kipherAes = KipherAes()

        val secretKey = kipherAes.generateKey()
        val cipherText = kipherAes.encrypt(message, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test encryption with metadata`() {
        val kipherAes = KipherAes()

        val secretKey = kipherAes.generateKey()
        val cipherText = kipherAes.encrypt(message, aad, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, aad, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test encryption with wrong metadata`() {
        val kipherAes = KipherAes()

        val secretKey = kipherAes.generateKey()
        val cipherText = kipherAes.encrypt(message, aad, secretKey)

        assertThrows<KipherException> {
            kipherAes.decrypt(cipherText, "wrong-metadata".encodeToByteArray(), secretKey)
        }
    }

    @Test
    fun `test encryption with metadata using invalid secret key`() {
        val kipherAes = KipherAes()

        assertThrows<KipherException> {
            kipherAes.encrypt(message, aad, invalidKey)
        }
    }

    @Test
    fun `test encryption with invalid secret key`() {
        val kipherAes = KipherAes()

        assertThrows<KipherException> {
            kipherAes.encrypt(message, invalidKey)
        }
    }

    @Test
    fun `test encryption with custom key size`() {
        val kipherAes = KipherAes(192)

        val secretKey = kipherAes.generateKey()
        val cipherText = kipherAes.encrypt(message, aad, secretKey)
        val decrypted = kipherAes.decrypt(cipherText, aad, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test encryption with invalid custom key size`() {
        val kipherAes = KipherAes(999)

        val secretKey = kipherAes.generateKey()

        assertThrows<KipherException> {
            kipherAes.encrypt(message, aad, secretKey)
        }
    }

    @Test
    fun `test decryption with invalid secret key`() {
        val kipherAes = KipherAes()

        val secretKey = kipherAes.generateKey()
        val cipherText = kipherAes.encrypt(message, secretKey)

        assertThrows<KipherException> {
            kipherAes.decrypt(cipherText, invalidKey)
        }
    }
}
