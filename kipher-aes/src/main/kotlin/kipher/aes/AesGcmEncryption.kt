package kipher.aes

import kipher.aes.interfaces.AesEncryptionInterface
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

// Constants
private const val ALGORITHM = "AES"
private const val GCM_IV_LENGTH = 12
private const val GCM_KEY_LENGTH = 16
private const val GCM_KEY_ROUND = 8
private const val AES_KEY_SIZE = 256
private const val AES_MODE = "AES/GCM/NoPadding"

/**
 * Encryption using AES/GCM/NoPadding with optional metadata verification
 *
 * The Initialization Vector (IV) is generated randomly and prepended to the cipher text.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 */
class AesGcmEncryption : AesEncryptionInterface {
    private val secureRandom = SecureRandom()
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    init {
        keyGenerator.init(AES_KEY_SIZE, secureRandom)
    }

    /*
     * Generate a random byte array of 12 bytes
     * used for generating a random IV
     */
    private fun generateIv(): ByteArray {
        val iv = ByteArray(GCM_IV_LENGTH)
        secureRandom.nextBytes(iv)

        return iv
    }

    /**
     * Generate a secret key
     *
     * @return secret key as [ByteArray]
     */
    override fun generateKey(): ByteArray {
        return keyGenerator.generateKey().encoded
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
            // randomize iv for each encryption
            val iv = generateIv()

            val cipher = Cipher.getInstance(AES_MODE)
            val parameterSpec = GCMParameterSpec(GCM_KEY_LENGTH * GCM_KEY_ROUND, iv)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
            val cipherText = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))

            // concatenate iv and cipher text
            val byteBuffer = ByteBuffer.allocate(iv.size + cipherText.size)
            byteBuffer.put(iv)
            byteBuffer.put(cipherText)
            byteBuffer.array()
        } catch (e: GeneralSecurityException) {
            throw AesEncryptionException(e)
        }
    }

    /**
     * Encrypts the provided string with metadata.
     *
     * @param data data to encrypt
     * @param metadata metadata to encrypt with
     * @param key  secret key to encrypt with
     * @return encrypted message
     *
     * @throws RuntimeException if encryption fails
     */
    @Throws(RuntimeException::class)
    override fun encrypt(data: String, metadata: ByteArray, key: ByteArray): ByteArray {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()

            val cipher = Cipher.getInstance(AES_MODE)
            val parameterSpec = GCMParameterSpec(GCM_KEY_LENGTH * GCM_KEY_ROUND, iv)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)

            // encrypt data along with the metadata
            cipher.updateAAD(metadata)
            val cipherText = cipher.doFinal(data.toByteArray(StandardCharsets.UTF_8))

            // concatenate iv and cipher text
            val byteBuffer = ByteBuffer.allocate(iv.size + cipherText.size)
            byteBuffer.put(iv)
            byteBuffer.put(cipherText)
            byteBuffer.array()
        } catch (e: GeneralSecurityException) {
            throw AesEncryptionException(e)
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
            val gcmIv = GCMParameterSpec(GCM_KEY_LENGTH * GCM_KEY_ROUND, encrypted, 0, GCM_IV_LENGTH)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

            // Use everything from 12 bytes on as ciphertext
            cipher.doFinal(encrypted, GCM_IV_LENGTH, encrypted.size - GCM_IV_LENGTH)
        } catch (e: GeneralSecurityException) {
            throw AesEncryptionException(e)
        }
    }

    /**
     * Decrypts encrypted message with metadata verification.
     *
     * @param encrypted message/data to be decrypted
     * @param key       secret key used to encrypt
     * @return original plaintext
     *
     * @throws RuntimeException if decryption fails, usually due to invalid/mismatched key
     */
    @Throws(RuntimeException::class)
    override fun decrypt(encrypted: ByteArray, metadata: ByteArray, key: ByteArray): ByteArray {
        return try {
            val cipher = Cipher.getInstance(AES_MODE)

            // use first 12 bytes for IV
            val gcmIv = GCMParameterSpec(GCM_KEY_LENGTH * GCM_KEY_ROUND, encrypted, 0, GCM_IV_LENGTH)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

            // check if metadata is correct/matches
            cipher.updateAAD(metadata)

            // Use everything from 12 bytes on as ciphertext
            cipher.doFinal(encrypted, GCM_IV_LENGTH, encrypted.size - GCM_IV_LENGTH)
        } catch (e: GeneralSecurityException) {
            throw AesEncryptionException(e)
        }
    }
}
