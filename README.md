# Kipher Cryptography Library

[![Codecov](https://img.shields.io/codecov/c/github/jhdcruz/kipher?token=bCmx2D264p&style=for-the-badge&logo=codecov&label=Coverage&labelColor=black&color=blue)](https://app.codecov.io/gh/jhdcruz/kipher)

**Abstracted cryptographic library for straightforward & hassle-free cryptographic
operations for JVM applications.**

This library compliments with Java's JCE but does not necessarily aim for 1:1
functionality & compatibility in certain cases, this library is in some form **opinionated**
but tries to offer customizablility as much as possible.

### Features:

- [Bouncy Castle](https://bouncycastle.org/) Security
  Provider <sup>([Configurable](#using-different-security-provider))</sup>

- [Symmetric Encryption](./kipher-symmetric/README.md) (AES, ChaCha20, etc.)
- [Message Digests](./kipher-digest/README.md) (MD5, SHA, SHA3, etc.)
- [MACs](./kipher-mac/README.md) (HMAC, Poly1305 etc.)

- and more to be implemented...

## Requirements

Minimum requirements to use the library:

- Kotlin 1.8+
- Java 11+

## Usage

| Modules                                                                                                                                                                                                                                                                                                  | Description                                                     | Links                                     |
| :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :-------------------------------------------------------------- | ----------------------------------------- |
| [![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-core?style=flat-square&logo=apachemaven&label=kipher-core&labelColor=black&color=blue&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-core)](./kipher-core/README.md)                     | Core utilities for the library. **(Internal use)**              | [API](https://kipher-core.pages.dev)      |
| [![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-symmetric?style=flat-square&logo=apachemaven&label=kipher-symmetric&labelColor=black&color=blue&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-symmetric)](./kipher-symmetric/README.md) | Data encryption using symmetric ciphers. (AES, ChaCha20, etc.). | [API](https://kipher-symmetric.pages.dev) |
| [![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-digest?style=flat-square&logo=apachemaven&label=kipher-digest&labelColor=black&color=blue&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-digest)](./kipher-digest/README.md)             | Cryptographic hash functions (SHAs, MD5s, etc.).                | [API](https://kipher-digest.pages.dev)    |
| [![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-mac?style=flat-square&logo=apachemaven&label=kipher-mac&labelColor=black&color=blue&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-mac)](./kipher-mac/README.md)                         | Data integrity and authentication using MACs.                   | [API](https://kipher-mac.pages.dev)       |

### Gradle

```kotlin
implementation("io.github.jhdcruz:kipher-$module:$version")
```

### Maven

```xml
<depenedencies>
    <dependency>
        <groupId>io.github.jhdcruz</groupId>
        <artifactId>kipher-$module</artifactId>
        <version>$version</version>
    </dependency>
</depenedencies>
```

<details>
  <summary>
    Using Snapshots
  </summary>

### Snapshots

Look at module's `gradle.properties` to check for latest snapshot version.

#### Gradle

```kotlin
repositories {
    mavenCentral()

    // add snapshot repo
    maven {
        url("https://s01.oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation("io.github.jhdcruz:kipher-$module:0.2.0-SNAPSHOT")
}
```

#### Maven

```xml
<project>
    <repositories>
        <repository>
            <id>sonatype-snapshots</id>
            <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>io.github.jhdcruz</groupId>
            <artifactId>kipher-$module</artifactId>
            <version>0.2.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

> [!WARNING]\
> Snapshots should be considered unstable and contains breaking changes,
> they are primarily for testing purposes, and **not for production use**.

</details>

### Kotlin

```kotlin
import io.github.jhdcruz.kipher.symmetric.aes.AesGCM


fun main() {
    val encryptionUtils = AesGCM()

    val data = "sample data".encodeToByteArray()
    val tag = "sample aad".encodeToByteArray()

    // named parameters are recommended, but optional
    val encrypted = encryptionUtils.encrypt(
        data = message,
        tag = tag,
        // optional `key` parameter
    ) // returns Map of [data, key, tag]

    val decrypted = encryptionUtils.decrypt(encrypted)

    // or, manually:

    val decrypted = encryptionUtils.decrypt(
        encrypted = encrypted.getValue("data"),
        key = encrypted.getValue("key")
        tag = encrypted.getValue("tag")
    )

    println(decryptedPass.toString())
}
```

### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.symmetric.aes.AesGCM;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        AesGCM encryptionUtils = new AesGCM();

        byte[] data = "Hello World".getBytes();

        Map<String, byte[]> encrypted = encryptionUtils.encrypt(data);

        byte[] val = encryptionUtils.decrypt(encrypted);

        // or

        byte[] val = encryptionUtils.decrypt(
            encrypted.get("data"),
            encrypted.get("key")
            encrypted.get("tag")
        );

        System.out.println(new String(val)); // outputs "Hello World"
    }
}
```

### Using different key size

```kotlin
import io.github.jhdcruz.kipher.symmetric.aes.AesCBC


fun main() {
    val encryptionUtils = AesCBC()

    val data = "sample data".encodeToByteArray()

    val secretKey: ByteArray = encryptionUtils.generateKey(128) // should be a valid one

    val encrypted = encryptionUtils.encrypt(
        data = message,
        key = secretKey
    )

    val decrypted = encryptionUtils.decrypt(encrypted)

    println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
}
```

### Using different security provider

Default security provider is set to [Bouncy Castle](https://bouncycastle.org/).

> [!NOTE]\
> Changing provider has to be done **before** using any of the library
> functions/methods.

#### Module-specific Provider

Example of changing provider for `kipher-symmetric`:

##### Kotlin

```kotlin
import io.github.jhdcruz.kipher.symmetric.SymmetricEncryption

import java.security.Provider
import java.security.Security

fun main() {
    // must be declared before using any symmetric ciphers methods!
    val provider: Provider = Security.getProvider("SunJCE")
    SymmetricEncryption.provider(provider)
}
```

##### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.symmetric.SymmetricEncryption;

import java.security.Provider;
import java.security.Security;

// For other JVM-based languages,
// adjust syntax based on language

class Main {
    public static void main(String[] args) {
        // must be declared only once before using any AES methods
        // or at the beginning of the app's main method or such.
        Provider provider = Security.getProvider("SunJCE");
        SymmetricEncryption.Companion.setProvider(provider);
        // and so on, so forth
    }
}
```

#### Global Provider

If desired, it is also possible to change provider that affects the entire library
modules.

##### Kotlin

```kotlin
import io.github.jhdcruz.kipher.aes.GcmEncryption
import io.github.jhdcruz.kipher.core.KipherProvider

import java.security.Provider
import java.security.Security

fun main() {
    // must be declared only once before using any library functions
    // or at the beginning of the app's main method or such.
    val provider: Provider = Security.getProvider("SunJCE")
    KipherProvider.provider = provider

    val encryptionUtils = GcmEncryption()
    // and so on, so forth
}
```

##### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.aes.GcmEncryption;
import io.github.jhdcruz.kipher.core.KipherProvider;

import java.security.Provider;
import java.security.Security;

// For other JVM-based languages,
// adjust syntax based on language

class Main {
    public static void main(String[] args) {
        // must be declared only once before using any AES methods
        // or at the beginning of the app's main method or such.
        Provider provider = Security.getProvider("SunJCE");
        KipherProvider.Companion.setProvider(provider);

        GcmEncryption encryptionUtils = GcmEncryption();
        // and so on, so forth
    }
}
```

> [!WARNING]\
> `provider` value is tied to the class itself, keep in mind when using the library
> functions/methods in parallel with different providers.

## Compatibility

I strive for backward-compatibility **as much as possible**, but due to the nature of this library
being a cryptographic library, even a very small change can introduce a breaking change incompatible
with previous versions.

### Versioning

The library will follow semantic versioning where every breaking changes bumps the major version
regardless of how small the change is, this way developers know that something will not work should
they update.

> [!NOTE]\
> Each modules are independently versioned to avoid version bumps between unrelated module/s.

## Contributing

If you want to contribute to this project, feel free to open an issue or discussion **before**
opening a pull request to avoid wasted efforts.

## License

[![FOSSA Status](https://app.fossa.com/api/projects/custom%2B26392%2Fgithub.com%2Fjhdcruz%2Fkipher.svg?type=small)](https://app.fossa.com/projects/custom%2B26392%2Fgithub.com%2Fjhdcruz%2Fkipher?ref=badge_small)

This project is licensed under the `Apache 2.0 License` - see the [LICENSE](./LICENSE.txt) file for
details

## Disclaimer

> [!IMPORTANT]\
> **I** ([@jhdcruz](https://github.com/jhdcruz)) **am not a security expert/professional**.

This library is primarily made for convenience and ease-of-use,
while providing as much security as possible out-of-the-box.

> If you found a security issue, please [report the security issue](./SECURITY.md).
