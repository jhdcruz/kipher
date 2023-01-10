@file:JvmSynthetic

package kipher.aes

/**
 * Exception thrown when an error occurs during encryption or decryption.
 *
 * @constructor creates a new [RuntimeException] with the given message and cause
 *
 * @param message the error message
 * @param cause the cause of the error
 */
class AesEncryptionException(message: String?, cause: Throwable?) : RuntimeException(message, cause)
