package aes

import interfaces.AesEncryptionInterface

import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Encryption and Decryption with AES/GCM/NoPadding
 */
class AesGcmEncryption : AesEncryptionInterface {
    private val secureRandom = SecureRandom()

    /**
     * This generates a random byte array of 16 bytes
     * Specifically used for generating a random key for AES encryption
     *
     * @return random 16 byte array
     */
    override fun generateSecretKey(): ByteArray {
        val key = ByteArray(GCM_TAG_LENGTH)
        secureRandom.nextBytes(key)

        return key
    }

    /**
     * Encrypts the provided string.
     *
     * @param plaintext to encrypt (UTF-8)
     * @return encrypted message
     */
    @Throws(RuntimeException::class)
    override fun encrypt(plaintext: String, secretKey: ByteArray): ByteArray {
        return try {
            // never reuse this iv with same key
            val iv = ByteArray(GCM_IV_LENGTH)
            secureRandom.nextBytes(iv)

            val cipher = Cipher.getInstance(AES_MODE)
            val parameterSpec = GCMParameterSpec(AES_KEY_SIZE, iv)
            val secretKeySpec = SecretKeySpec(secretKey, ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, parameterSpec)

            val cipherText = cipher.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
            val byteBuffer = ByteBuffer.allocate(iv.size + cipherText.size)

            byteBuffer.put(iv)
            byteBuffer.put(cipherText)
            byteBuffer.array()
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }

    /**
     * Decrypts encrypted message.
     *
     * @param cipherText iv with ciphertext
     * @return original plaintext
     */
    @Throws(RuntimeException::class)
    override fun decrypt(cipherText: ByteArray, secretKey: ByteArray): String {
        return try {
            val cipher = Cipher.getInstance(AES_MODE)

            // use first 12 bytes for iv
            val gcmIv: AlgorithmParameterSpec = GCMParameterSpec(AES_KEY_SIZE, cipherText, 0, GCM_IV_LENGTH)
            cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(secretKey, ALGORITHM), gcmIv)

            // Use everything from 12 bytes on as ciphertext
            val plainText = cipher.doFinal(cipherText, GCM_IV_LENGTH, cipherText.size - GCM_IV_LENGTH)

            String(plainText, StandardCharsets.UTF_8)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }

    companion object {
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 16

        private const val ALGORITHM = "AES"
        private const val AES_KEY_SIZE = 256
        private const val AES_MODE = "AES/GCM/NoPadding"
    }
}