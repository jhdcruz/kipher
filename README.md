# Kipher

[![Codacy coverage](https://img.shields.io/codacy/coverage/79a33e548aff4d96973084c99efaf462?color=%1E1E1E&label=Coverage&logo=codacy&style=flat-square)](https://app.codacy.com/gh/jhdcruz/kipher/dashboard)

**Abstracted cryptographic library for straightforward & hassle-free cryptographic
operations for JVM applications.**

This library compliments with Java's JCE but does not necessarily aim for 1:1
functionality & compatibility, this library is in some form **opinionated**
but tries to offer customizablility as much as possible.

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

## Requirements

Minimum requirements to use the library:

- Kotlin 1.7+
- Java 8+

> **Note**
>
> If your project uses earlier JDK 8, you might need
> [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](https://www.oracle.com/java/technologies/javase-jce-all-downloads.html)
> for the library to function properly.
>
> *See more: https://stackoverflow.com/a/3864276*

## Usage

> [API documentation](https://jhdcruz.github.io/kipher/)

| Modules                                                                                                                                                                                                                                                           | Description                                          |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-----------------------------------------------------|
| ![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-common?style=flat-square&logo=apachemaven&label=kipher-common&labelColor=black&color=violet&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-common) | Common utilities for the library. **(Internal use)** |
| ![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-aes?style=flat-square&logo=apachemaven&label=kipher-aes&labelColor=black&color=violet&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-aes)          | AES encryption utilities.                            |

### Gradle

```kotlin
implementation("io.github.jhdcruz:kipher-$module:$version") // Replace module & version
```

### Maven

```xml
<dependency>
    <groupId>io.github.jhdcruz</groupId>
    <artifactId>kipher-$module</artifactId>  <!-- Replace $module -->
    <version>$version</version>  <!-- Replace $version -->
</dependency>
```

### Snapshots

```kotlin
repositories {
    mavenCentral()

    // add snapshot repo
    maven {
        url("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("io.github.jhdcruz:kipher-$module:$version-SNAPSHOT")
}
```

> **Warning**
>
> Snapshots should be considered unstable and contain breaking changes,
> they are primarily for testing purposes.
>
> **Use at your own risk.**

<details>
<summary>Other ways to use the library</summary>

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

> This method doesn't include all the necessary dependencies.
>
> Although, the errors will tell you the dependencies you need.

</details>

### Kotlin

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

        val secretKey: ByteArray = encryptionUtils.generateKey(128) // should be a valid one

        val encrypted = gcmEncryption.encrypt(
            data = message,
            key = secretKey
        )

        val decrypted = gcmEncryption.decrypt(encrypted)

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

#### Methods

There are 2 methods you'll primarily use:

- `encrypt`
- `decrypt`

Easy and straightforward methods, but relies on internal implementation.

**You cannot decrypt a data that was encrypted by a different method or library**.
Unless they use the same internal implementation as this library.

##### Advanced Usage/Methods

- `encryptBare`
- `decryptBare`

Requires all the necessary data for the encryption/decryption process,
such as IV, key, AADs, whatever that is applicable.

**You can decrypt data that was encrypted by a different method or library**.
Unless they involve a different or custom implementation of the encryption/decryption process.

## Compatibility

I strive for backward-compatibility **as much as possible**, but due to the nature of this library
being a cryptographic library, even a very small change can introduce a breaking change incompatible
with previous versions.

The library will follow semantic versioning where breaking changes bumps the major version,
this way developers know that something might not work should they update.

## Contributing

If you want to contribute to this project, feel free to open an issue or discussion **before**
opening a pull request to avoid wasted efforts.

## License

[![FOSSA Status](https://app.fossa.com/api/projects/custom%2B26392%2Fgithub.com%2Fjhdcruz%2Fkipher.svg?type=small)](https://app.fossa.com/projects/custom%2B26392%2Fgithub.com%2Fjhdcruz%2Fkipher?ref=badge_small)

This project is licensed under the `Apache 2.0 License` - see the [LICENSE](./LICENSE.txt) file for
details

## Disclaimer

**I** ([@jhdcruz](https://github.com/jhdcruz)) **am not a security expert/professional**.

This library is primarily made for convenience and ease-of-use,
while providing as much security as possible out-of-the-box.

> If you found a security issue, please [report a security issue](./SECURITY.md).
