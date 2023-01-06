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

// Constants
private const val ALGORITHM = "AES"
private const val GCM_IV_LENGTH = 12
private const val GCM_KEY_LENGTH = 16
private const val AES_KEY_SIZE = 128
private const val AES_MODE = "AES/GCM/NoPadding"

/**
 * Encryption using AES/GCM/NoPadding
 */
class AesGcmEncryption : AesEncryptionInterface {
    private val secureRandom = SecureRandom()

    /**
     * This generates a random byte array of 16 bytes
     * Specifically used for generating a random key
     *
     * @return random 16 byte array
     */
    override fun generateKey(): ByteArray {
        val key = ByteArray(GCM_KEY_LENGTH)
        secureRandom.nextBytes(key)

        return key
    }

    /**
     * Encrypts the provided string.
     *
     * @param data data to encrypt
     * @param key  secret key to encrypt with
     * @return encrypted message
     *
     * @throws RuntimeException if encryption fails
     */
    @Throws(RuntimeException::class)
    override fun encrypt(data: String, key: ByteArray): ByteArray {
        return try {
            // never reuse this iv with same key
            val iv = ByteArray(GCM_IV_LENGTH)
            secureRandom.nextBytes(iv)

            val cipher = Cipher.getInstance(AES_MODE)
            val parameterSpec = GCMParameterSpec(AES_KEY_SIZE, iv)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
            val cipherText = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))

            // concatenate iv and cipher text
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
     * @param encrypted message/data to be decrypted
     * @param key       secret key used to encrypt
     * @return original plaintext
     *
     * @throws RuntimeException if decryption fails, usually due to invalid/mismatched key
     */
    @Throws(RuntimeException::class)
    override fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray {
        return try {
            val cipher = Cipher.getInstance(AES_MODE)

            // use first 12 bytes for iv
            val gcmIv: AlgorithmParameterSpec = GCMParameterSpec(AES_KEY_SIZE, encrypted, 0, GCM_IV_LENGTH)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

            // Use everything from 12 bytes on as ciphertext
            cipher.doFinal(encrypted, GCM_IV_LENGTH, encrypted.size - GCM_IV_LENGTH)

        } catch (e: GeneralSecurityException) {
            throw RuntimeException(e)
        }
    }
}