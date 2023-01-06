package interfaces

internal interface AesEncryptionInterface {
    fun generateKey(): ByteArray

    fun encrypt(data: String, key: ByteArray): ByteArray

    fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray
}