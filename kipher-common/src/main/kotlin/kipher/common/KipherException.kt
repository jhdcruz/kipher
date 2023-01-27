package kipher.common

/**
 * Throw custom exception caused by [cause].
 *
 * @constructor creates a new [RuntimeException] with the entire exception
 */
class KipherException : RuntimeException {
    constructor(e: Throwable?) : super(e)
    constructor(message: String) : super(message)
}
