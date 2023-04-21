/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.aes

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AesModesEncryptionTest {
    private val message = "message".encodeToByteArray()
    private val aad = "metadata".encodeToByteArray()

    private val decodeToString = { bytes: ByteArray -> String(bytes, Charsets.UTF_8) }

    @Test
    fun `test CBC encryption`() {
        val cbcEncryption = CbcEncryption()

        val secretKey = cbcEncryption.generateKey()
        val cipherText = cbcEncryption.encrypt(message, secretKey)
        val decrypted = cbcEncryption.decrypt(cipherText, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test GCM encryption`() {
        val gcmEncryption = GcmEncryption()

        val secretKey = gcmEncryption.generateKey()
        val cipherText = gcmEncryption.encrypt(message, aad, secretKey)
        val decrypted = gcmEncryption.decrypt(cipherText, aad, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CCM encryption`() {
        val ccmEncryption = CcmEncryption()

        val secretKey = ccmEncryption.generateKey()
        val cipherText = ccmEncryption.encrypt(message, aad, secretKey)
        val decrypted = ccmEncryption.decrypt(cipherText, aad, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CFB encryption`() {
        val cfbEncryption = CfbEncryption()

        val secretKey = cfbEncryption.generateKey()
        val cipherText = cfbEncryption.encrypt(message, secretKey)
        val decrypted = cfbEncryption.decrypt(cipherText, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }

    @Test
    fun `test CTR encryption`() {
        val ctrEncryption = CtrEncryption()

        val secretKey = ctrEncryption.generateKey()
        val cipherText = ctrEncryption.encrypt(message, secretKey)
        val decrypted = ctrEncryption.decrypt(cipherText, secretKey)

        assertEquals(decodeToString(message), decodeToString(decrypted))
    }
}
