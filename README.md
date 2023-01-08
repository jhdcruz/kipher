# Kipher

![Codacy coverage](https://img.shields.io/codacy/coverage/79a33e548aff4d96973084c99efaf462?color=%232459ED&label=Coverage&logo=codacy&style=for-the-badge) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/jhdcruz/kipher?color=green&logo=github&style=for-the-badge)

A simple library for data encryption in Java/Kotlin.

### Features:

- AES
    - AES/GCM/NoPadding
- and more to be implemented...

The goal of this library is to provide an abstraction layer for encrypting and decrypting data in Java/Kotlin. It is a
helper for encrypting and decrypting data in a straightforward and hassle-free method.

See [Usage](#usage).

## Usage

Unfortunately, The library is not **yet** available in Maven Central.

Here's how you can use it:

- Download the latest `.jar` release from [here](https://github.com/jhdcruz/kipher/releases/latest), and manually add it
  to your
  project. [Eclipse](https://stackoverflow.com/questions/2824515/how-to-add-external-library-properly-in-eclipse) | [IntelliJ IDEA](https://www.jetbrains.com/help/idea/library.html#define-library) | [Netbeans](https://stackoverflow.com/questions/4879903/how-to-add-a-jar-in-netbeans)
- You can use [JitPack](https://jitpack.io/) to add the library in your project.

### Kotlin:

Using the library in kotlin is as easy as importing it:

```kotlin
import io.github.jhdcruz.kipher.aes.AesGcmEncryption

class EncryptionTest {
    fun main() {
        val encryptionUtils = AesGcmEncryption()
        val data = "sample data"
        val secretKey = encryptionUtils.generateSecretKey()

        val encryptedData: ByteArray = encryptionUtils.encrypt(data, secretKey)
        val decryptedPass: ByteArray = encryptionUtils.decrypt(encryptedData, secretKey)

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

### Java:

Using the library in Java requires a few things that needs to be done first:

1. [`kotlin-stdlib`](https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib/1.8.0) is required to be
   added as a dependency.

```java
import io.github.jhdcruz.kipher.aes.AesGcmEncryption;

class EncryptionTest {
    public static void main(String[] args) {
        AesGcmEncryption encryptionUtils = new AesGcmEncryption();

        String data = "sample data";
        SecretKey secretKey = encryptionUtils.generateSecretKey();

        bytes[] encryptedData = encryptionUtils.encrypt(data, secretKey);
        bytes[] decryptedPass = encryptionUtils.decrypt(encryptedData, secretKey);

        System.out.println(new String(decryptedPass, StandardCharsets.UTF_8)); // outputs "sample data"
    }
}
```

#### Compatibility

The library is compatible with Java 8+. **However**, if your project uses JDK 8, you might need
[Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](https://www.oracle.com/java/technologies/javase-jce-all-downloads.html)
for the library to function properly.

### Importing

It is very advisable to import only the encryption class/es you need:

```kotlin
import io.github.jhdcruz.kipher.aes.AesGcmEncryption
```

Instead of doing:

```kotlin
import io.github.jhdcruz.kipher.*
```

> Visit the API documentation [here](https://jhdcruz.github.io/kipher/).
>
> Friendly documentation is still a work in progress...

## Contributing

If you want to contribute to this project, feel free to open an issue or a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE.txt) file for details

## Disclaimer

I am not a security expert/guru, this library is primarily made for ease-of-use, while implementing strong encryption
methods as much as possible out-of-the-box. If you found a security issue, please
see [reporting a security issue](./SECURITY.md).
