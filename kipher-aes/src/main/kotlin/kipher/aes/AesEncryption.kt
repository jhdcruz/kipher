package kipher.aes

import kipher.aes.internal.AesEncryptionInterface
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.GeneralSecurityException
import java.security.InvalidParameterException
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// Constants
private const val ALGORITHM = "AES"
private const val IV_LENGTH = 16

private const val GCM_TAG_LENGTH = 128
private const val GCM_IV_LENGTH = 12

/**
 * Encryption using AES.
 *
 * The Initialization Vector (IV) is generated randomly and prepended to the cipher text.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 * @param aesMode Custom [AesModes] (default: [AesModes.GCM])
 */
class AesEncryption(keySize: Int = 256, aesMode: AesModes = AesModes.GCM) : AesEncryptionInterface {
    private val secureRandom = SecureRandom()
    private val keyGenerator: KeyGenerator = KeyGenerator.getInstance(ALGORITHM)

    private val transformation = aesMode.mode
    private val ivLength: Int

    init {
        try {
            keyGenerator.init(keySize, secureRandom)

            // set required iv length based on mode
            ivLength = when (aesMode) {
                AesModes.GCM -> GCM_IV_LENGTH
                else -> IV_LENGTH
            }
        } catch (e: InvalidParameterException) {
            throw AesEncryptionException(e)
        }
    }

    /** Generate a pseudo-random IV. */
    override fun generateIv(): ByteArray {
        // other modes uses 16 iv size while GCM uses 12
        val iv = if (transformation != AesModes.GCM.mode) {
            ByteArray(IV_LENGTH)
        } else {
            ByteArray(GCM_IV_LENGTH)
        }

        secureRandom.nextBytes(iv)
        return iv
    }

    /** Generate a secret key as [ByteArray]. */
    override fun generateKey(): ByteArray {
        return keyGenerator.generateKey().encoded
    }

    /**
     * Encrypts the provided [data] using the provided [key].
     *
     * @throws AesEncryptionException
     */
    @Throws(AesEncryptionException::class)
    override fun encrypt(data: String, key: ByteArray): ByteArray {
        return try {
            // randomize iv for each encryption
            val iv = generateIv()

            val cipher = Cipher.getInstance(transformation)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            // Only GCM has a specific method compared to other modes
            if (transformation == AesModes.GCM.mode) {
                val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, parameterSpec)
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, IvParameterSpec(iv))
            }

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
     * Encrypts the provided [data] along with [metadata] using [key].
     *
     * Only supports [AesModes.GCM].
     *
     * @throws AesEncryptionException
     */
    @Throws(AesEncryptionException::class)
    override fun encrypt(data: String, metadata: ByteArray, key: ByteArray): ByteArray {
        if (transformation != AesModes.GCM.mode) {
            throw AesEncryptionException("Metadata is only supported in GCM mode.")
        } else {
            return try {
                // randomize iv for each encryption
                val iv = generateIv()

                val cipher = Cipher.getInstance(transformation)
                val keySpec = SecretKeySpec(key, ALGORITHM)

                val parameterSpec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
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
    }

    /**
     * Decrypts [encrypted] data using [key].
     *
     * @throws AesEncryptionException
     */
    @Throws(AesEncryptionException::class)
    override fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray {
        return try {
            val cipher = Cipher.getInstance(transformation)
            val keySpec = SecretKeySpec(key, ALGORITHM)

            if (transformation == AesModes.GCM.mode) {
                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(GCM_TAG_LENGTH, encrypted, 0, ivLength)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)
            } else {
                cipher.init(Cipher.DECRYPT_MODE, keySpec, IvParameterSpec(encrypted, 0, ivLength))
            }

            // Use everything from 12 bytes on as ciphertext
            cipher.doFinal(
                encrypted,
                ivLength,
                encrypted.size - ivLength
            )
        } catch (e: GeneralSecurityException) {
            throw AesEncryptionException(e)
        }
    }

    /**
     * Decrypts [encrypted] data with [metadata] verification using [key].
     *
     * Only supports [AesModes.GCM].
     *
     * @throws AesEncryptionException
     */
    @Throws(AesEncryptionException::class)
    override fun decrypt(encrypted: ByteArray, metadata: ByteArray, key: ByteArray): ByteArray {
        if (transformation != AesModes.GCM.mode) {
            throw AesEncryptionException("Metadata is only supported in GCM mode.")
        } else {
            return try {
                val cipher = Cipher.getInstance(transformation)
                val keySpec = SecretKeySpec(key, ALGORITHM)

                // use first 12 bytes for iv
                val gcmIv = GCMParameterSpec(GCM_TAG_LENGTH, encrypted, 0, ivLength)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmIv)

                // check if metadata is correct/matches
                cipher.updateAAD(metadata)

                // Use everything from 12 bytes on as ciphertext
                cipher.doFinal(
                    encrypted,
                    ivLength,
                    encrypted.size - ivLength
                )
            } catch (e: GeneralSecurityException) {
                throw AesEncryptionException(e)
            }
        }
    }
}
