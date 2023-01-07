# Kipher

A simple library helper for encrypting and decrypting data in Java/Kotlin.

**Features:**

- AES
    - AES/GCM/NoPadding
- and more to be implemented...

The goal of this library is to provide an abstraction layer for encrypting and decrypting data in Java/Kotlin. It is a
helper for encrypting and decrypting data in a straightforward and hassle-free method.

See [Usage](#usage).

> **Disclaimer:**
>
> I am not a security expert/guru, this library is primarily made for convenience and productivity, while adhering
> to strong encryption methods. If you found a security issue, please see [reporting a security issue](./SECURITY.md).

## Usage

Visit the code documentation [here](https://jhdcruz.github.io/kipher/).

```kotlin
import io.github.jhdcruz.kipher.aes.AesGcmEncryption

class EncryptionTest {
    fun main() {
        val encryptionUtils = AesGcmEncryption()
        val data = "sample data"
        val secretKey = encryptionUtils.generateSecretKey()

        val encryptedData = encryptionUtils.encrypt(data, secretKey)
        val decryptedPass = encryptionUtils.decrypt(passwordHash, secretKey)
    }
}
```

> Friendly documentation is still a work in progress...

## Contributing

If you want to contribute to this project, feel free to open an issue or a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE.txt) file for details
