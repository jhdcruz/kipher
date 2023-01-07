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
> as much as possible to strong encryption methods. If you found a security issue,
> please see [reporting a security issue](./SECURITY.md).

## Usage

Visit the code documentation [here](https://jhdcruz.github.io/kipher/).

```kotlin
import io.github.jhdcruz.kipher.aes.AesGcmEncryption

class EncryptionTest {
    fun main() {
        val encryptionUtils = AesGcmEncryption()
        val data = "sample data"
        val secretKey = encryptionUtils.generateSecretKey()

        val encrypted = encryptionUtils.encrypt(data, secretKey)
        val decrypted = encryptionUtils.decrypt(encrypted, secretKey)
        
        println(decrypted, Charsets.UTF_8) // output: "sample data"
    }
}
```

> Friendly documentation is still a work in progress...

### Compatibility

**This library can be used in both Java 8+ and Kotlin 1.7+ projects.**

However, if you are currently developing in Java/JDK 8, you might need
[Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](https://www.oracle.com/java/technologies/javase-jce-all-downloads.html)

## Contributing

If you want to contribute to this project, feel free to open an issue or a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE.txt) file for details
