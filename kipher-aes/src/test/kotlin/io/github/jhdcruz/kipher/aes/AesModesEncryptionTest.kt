package io.github.jhdcruz.kipher.aes

import io.github.jhdcruz.kipher.aes.AesParams.aad
import io.github.jhdcruz.kipher.aes.AesParams.decodeToString
import io.github.jhdcruz.kipher.aes.AesParams.message
import java.security.Provider
import java.security.Security
import kotlin.test.Test
import kotlin.test.assertEquals

internal class AesModesEncryptionTest {

    // Explicitly set provider due to
    // AesGeneralTest's test different providers
    // which changes the provider for subsequent tests
    init {
        val provider: Provider = Security.getProvider("BC")
        AesEncryption.Companion.provider = provider
    }

    @Test
    fun `test CBC encryption with PKCS5`() {
        val cbcEncryption = CbcEncryption()

        val encrypted = cbcEncryption.encrypt(message)
        val decrypted = cbcEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CBC encryption with PKCS7`() {
        val cbcEncryption = Cbc7Encryption()

        val encrypted = cbcEncryption.encrypt(message)
        val decrypted = cbcEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test GCM encryption`() {
        val gcmEncryption = GcmEncryption()

        val encrypted =
            gcmEncryption.encrypt(
                data = message,
                aad = aad,
            )

        val decrypted = gcmEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CCM encryption`() {
        val ccmEncryption = CcmEncryption()

        val encrypted =
            ccmEncryption.encrypt(
                data = message,
                aad = aad,
            )

        val decrypted = ccmEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CFB encryption`() {
        val cfbEncryption = CfbEncryption()

        val encrypted = cfbEncryption.encrypt(message)
        val decrypted = cfbEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CTR encryption`() {
        val ctrEncryption = CtrEncryption()

        val encrypted = ctrEncryption.encrypt(message)
        val decrypted = ctrEncryption.decrypt(encrypted)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }
}
