package io.github.jhdcruz.kipher.aes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class AesModesEncryptionTest {
    private val message = "message".encodeToByteArray()
    private val aad = "metadata".encodeToByteArray()

    private val decodeToString = { bytes: ByteArray -> String(bytes, Charsets.UTF_8) }

    @Test
    fun `test CBC encryption`() {
        val cbcEncryption = CbcEncryption()

        val encrypted = cbcEncryption.encrypt(message)

        val decrypted = cbcEncryption.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test GCM encryption`() {
        val gcmEncryption = GcmEncryption()

        val encrypted = gcmEncryption.encrypt(
            data = message,
            aad = aad,
        )

        val decrypted = gcmEncryption.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CCM encryption`() {
        val ccmEncryption = CcmEncryption()

        val encrypted = ccmEncryption.encrypt(
            data = message,
            aad = aad,
        )

        val decrypted = ccmEncryption.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CFB encryption`() {
        val cfbEncryption = CfbEncryption()

        val encrypted = cfbEncryption.encrypt(message)

        val decrypted = cfbEncryption.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CTR encryption`() {
        val ctrEncryption = CtrEncryption()

        val encrypted = ctrEncryption.encrypt(message)

        val decrypted = ctrEncryption.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }
}
