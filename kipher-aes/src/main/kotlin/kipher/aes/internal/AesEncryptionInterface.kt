@file:JvmSynthetic

package kipher.aes.internal

internal interface AesEncryptionInterface {
    fun generateKey(): ByteArray

    fun encrypt(data: String, key: ByteArray): ByteArray
    fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray

    // With metadata
    fun encrypt(data: String, metadata: ByteArray, key: ByteArray): ByteArray
    fun decrypt(encrypted: ByteArray, metadata: ByteArray, key: ByteArray): ByteArray
}
