package io.github.jhdcruz.kipher.interfaces

internal interface AesEncryptionInterface {
    fun generateIv(): ByteArray
    fun generateKey(): ByteArray

    fun encrypt(data: String, key: ByteArray): ByteArray
    fun decrypt(encrypted: ByteArray, key: ByteArray): ByteArray

    // With metadata
    fun encrypt(data: String, metadata: ByteArray, key: ByteArray): ByteArray
    fun decrypt(encrypted: ByteArray, metadata: ByteArray, key: ByteArray): ByteArray
}