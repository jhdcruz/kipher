# Kipher

[![Codacy coverage](https://img.shields.io/codacy/coverage/79a33e548aff4d96973084c99efaf462?color=%1E1E1E&label=Coverage&logo=codacy&style=flat-square)](https://app.codacy.com/gh/jhdcruz/kipher/dashboard)

Abstracted cryptographic library for straightforward & hassle-free cryptographic
operations for JVM applications.

### Features:

#### Encryption

- [Bouncy Castle](https://bouncycastle.org/) Security Provider

- AES
    - `AES/GCM/NoPadding` _(Recommended)_
    - `AES/CCM/NoPadding`
    - `AES/CBC/PKCS7Padding`
    - `AES/CTR/NoPadding`
    - `AES/CFB/NoPadding`

- and more to be implemented...

> **Note**
>
> If you don't know which one to use, stick with the `recommended`
> based on your chosen encryption method.

## Usage

> [API documentation](https://jhdcruz.github.io/kipher/)

Unfortunately, **The library is not yet available in Maven Central.**

<details>
<summary>Other ways to use the library</summary>

These methods are untested, but should work.

- You can use [JitPack](https://jitpack.io/) to add the library in your project.

- Using the package directly from GitHub
  Packages.
    - [Gradle](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package)
    - [Maven](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry#installing-a-package)

- Download the latest `.jar` release from [here](https://github.com/jhdcruz/kipher/releases/latest),
  and manually add it to your project.
    - [Eclipse](https://stackoverflow.com/questions/2824515/how-to-add-external-library-properly-in-eclipse)
    - [IntelliJ IDEA](https://www.jetbrains.com/help/idea/library.html#define-library)
    - [Netbeans](https://stackoverflow.com/questions/4879903/how-to-add-a-jar-in-netbeans)
  > This method also requires `kipher-common`.

</details>

### Kotlin

Using the library in kotlin is as easy as importing it:

```kotlin
import io.github.jhdcruz.kipher.aes.GcmEncryption

class EncryptionTest {
    fun main() {
        val encryptionUtils = GcmEncryption()

        val data = "sample data".encodeToByteArray()
        val aad = "sample aad".encodeToByteArray()

        // named parameters are recommended, but optional
        val encrypted = gcmEncryption.encrypt(
            data = message,
            aad = aad,
        ) // returns Map<String, ByteArray> of [data, key]

        val decrypted = gcmEncryption.decrypt(encrypted)

        // or

        val decrypted = gcmEncryption.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.aes.GcmEncryption;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        GcmEncryption encryptionUtils = new GcmEncryption();

        byte[] data = "Hello World".getBytes();

        Map<String, byte[]> encrypted = encryptionUtils.encrypt(data);

        byte[] val = encryptionUtils.decrypt(encrypted);

        // or

        byte[] val = encryptionUtils.decrypt(
            encrypted.get("data"),
            encrypted.get("key")
        );

        System.out.println(new String(val)); // outputs "Hello World"
    }
}
```

### Using different key size

```kotlin
import io.github.jhdcruz.kipher.aes.GcmEncryption

class EncryptionTest {
    fun main() {
        val encryptionUtils = CbcEncryption()

        val data = "sample data".encodeToByteArray()
        val secretKey: ByteArray = encryptionUtils.generateKey(128)

        val encrypted = gcmEncryption.encrypt(
            data = message,
            key = secretKey
        )

        val decrypted = gcmEncryption.decrypt(encrypted)

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

> **Note**
>
> If your project uses earlier JDK 8, you might need
> [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](https://www.oracle.com/java/technologies/javase-jce-all-downloads.html)
> for the library to function properly.
>
> *See more: https://stackoverflow.com/a/3864276*

#### Methods

There are 4 methods you'll primarily use:

- `encrypt`
- `decrypt`

These are the most easy and straightforward methods you'll use,
but they rely on internal implementation, which means **you cannot
decrypt a data that was encrypted by a different method or library**.
Unless they use the same internal implementation as this library.

##### Advanced Usage/Methods

- `encryptBare`
- `decryptBare`

The parameters requires all the necessary data for the encryption/decryption process
individually such as IV, key, AADs, whatever that is applicable. Here **you can decrypt
data that was encrypted by a different method or library**. Unless they involve a different
or custom implementation of the encryption/decryption process.

## Compatibility

I strive for backward-compatibility **as much as possible**, but due to the nature of this library
being a cryptographic library, even a very small change can introduce a breaking change incompatible
with previous versions.

The library will follow semantic versioning where breaking changes bumps the major version,
this way developers know that something might not work should they update.

## Contributing

If you want to contribute to this project, feel free to open an issue or discussion **before**
opening a pull
request to avoid wasted efforts.

## License

[![FOSSA Status](https://app.fossa.com/api/projects/custom%2B26392%2Fgithub.com%2Fjhdcruz%2Fkipher.svg?type=small)](https://app.fossa.com/projects/custom%2B26392%2Fgithub.com%2Fjhdcruz%2Fkipher?ref=badge_small)

This project is licensed under the `Apache 2.0 License` - see the [LICENSE](./LICENSE.txt) file for
details

## Disclaimer

**I** ([@jhdcruz](https://github.com/jhdcruz)) **am not a security expert/professional**, this
library is primarily made for convenience and ease-of-use, while providing as much security as
possible out-of-the-box.

> If you found a security issue, please see [reporting a security issue](./SECURITY.md).
