@file:JvmSynthetic

package kipher.aes

internal interface AesEncryptionInterface {
    /** Generate a secret key as [ByteArray]. */
    fun generateKey(): ByteArray

    /** Encrypts the provided [data] using the provided [key]. */
    fun encrypt(data: String, key: ByteArray): ByteArray

    /** Decrypts [encrypted] data using [key]. */
    fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray

    /** Encrypts the provided [data] along with [metadata] using [key]. */
    fun encrypt(data: String, metadata: ByteArray, key: ByteArray): ByteArray

    /** Decrypts [encrypted] data with [metadata] verification using [key]. */
    fun decrypt(encrypted: ByteArray, metadata: ByteArray, key: ByteArray): ByteArray
}
