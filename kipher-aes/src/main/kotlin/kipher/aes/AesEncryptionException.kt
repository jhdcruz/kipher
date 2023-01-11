@file:JvmSynthetic

package kipher.aes

/**
 * Throw exception caused by [cause].
 *
 * @constructor creates a new [RuntimeException] with the given message and cause
 *
 */
class AesEncryptionException(cause: Throwable?) : RuntimeException(cause)
