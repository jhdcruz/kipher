# Kipher

![Codacy coverage](https://img.shields.io/codacy/coverage/79a33e548aff4d96973084c99efaf462?color=%232459ED&label=Coverage&logo=codacy&style=for-the-badge) ![GitHub release (latest by date)](https://img.shields.io/github/v/release/jhdcruz/kipher?color=green&logo=github&style=for-the-badge)

Opinionated library for straightforward & hassle-free data encryption in Java/Kotlin.

### Features:

#### Encryption

- AES
    - AES/GCM/NoPadding _(default)_
    - AES/CBC/PKCS5Padding
- and more to be implemented...

The goal of this library is to provide an abstraction layer for easily encrypting and decrypting data
in a straightforward and hassle-free method.

----

> **Warning**
>
> `Kipher` is still in early development, lots of breaking changes **will** occur.
>
> Pin to a specific version to avoid breaking changes, and avoid updating without looking at changelogs for now.

## Usage

> [API documentation](https://jhdcruz.github.io/kipher/) | Project Documentation<sup>(WIP)</sup>

Unfortunately, The library is not **yet** available in Maven Central.

Here's how you can use it:

- Download the latest `.jar` release from [here](https://github.com/jhdcruz/kipher/releases/latest), and manually add it
  to your
  project. ([Eclipse](https://stackoverflow.com/questions/2824515/how-to-add-external-library-properly-in-eclipse) | [IntelliJ IDEA](https://www.jetbrains.com/help/idea/library.html#define-library) | [Netbeans](https://stackoverflow.com/questions/4879903/how-to-add-a-jar-in-netbeans))

- You can use [JitPack](https://jitpack.io/) to add the library in your project.

- Using the package directly from GitHub
  Packages. ([Gradle](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package) | [Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package))

### Kotlin:

Using the library in kotlin is as easy as importing it:

```kotlin
import kipher.aes.AesEncryption

class EncryptionTest {
    fun main() {
        val encryptionUtils = AesEncryption()
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

1. [`kotlin-stdlib`](https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib/1.8.10) might be required
   to be added as a dependency.

```java
import kipher.aes.AesEncryption;

class EncryptionTest {
    public static void main(String[] args) {
        AesEncryption encryptionUtils = new AesEncryption();

        String data = "sample data";
        SecretKey secretKey = encryptionUtils.generateKey();

        byte[] encryptedData = encryptionUtils.encrypt(data, secretKey);
        byte[] decryptedPass = encryptionUtils.decrypt(encryptedData, secretKey);

        System.out.println(new String(decryptedPass, StandardCharsets.UTF_8)); // outputs "sample data"
    }
}
```

### Using different modes:

```kotlin
import kipher.aes.AesEncryption
import kipher.aes.AesModes

class EncryptionTest {
    fun main() {
        val encryptionUtils = AesEncryption(256, AesModes.CBC) // key size, mode
        val data = "sample data"
        val secretKey = encryptionUtils.generateKey()

        val encryptedData: ByteArray = encryptionUtils.encrypt(data, secretKey)
        val decryptedPass: ByteArray = encryptionUtils.decrypt(encryptedData, secretKey)

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

> **Note**
>
> If your project uses JDK 8, you might need
> [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](https://www.oracle.com/java/technologies/javase-jce-all-downloads.html)
> for the library to function properly.

## Contributing

If you want to contribute to this project, feel free to open an issue or a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE.txt) file for details

## Disclaimer

I am not a security expert/guru, this library is primarily made for ease-of-use, while implementing strong encryption
methods as much as possible out-of-the-box. If you found a security issue, please
see [reporting a security issue](./SECURITY.md).
