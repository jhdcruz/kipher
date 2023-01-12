@file:JvmSynthetic

package kipher.aes

/**
 * Throw custom exception caused by [cause].
 *
 * @constructor creates a new [RuntimeException] with the entire exception
 */
class AesEncryptionException : RuntimeException {
    constructor(e: Throwable?) : super(e)
    constructor(message: String) : super(message)
}
