package interfaces

interface AesEncryptionInterface {
    fun generateSecretKey(): ByteArray

    fun encrypt(plaintext: String, secretKey: ByteArray): ByteArray

    fun decrypt(cipherText: ByteArray, secretKey: ByteArray): String
}