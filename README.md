# Kipher

[![Codecov](https://img.shields.io/codecov/c/github/jhdcruz/kipher?token=bCmx2D264p&style=for-the-badge&logo=codecov&label=Coverage&labelColor=black&color=blue)](https://app.codecov.io/gh/jhdcruz/kipher)

**Abstracted cryptographic library for straightforward & hassle-free cryptographic
operations for JVM applications.**

This library compliments with Java's JCE but does not necessarily aim for 1:1
functionality & compatibility, this library is in some form **opinionated**
but tries to offer customizablility as much as possible.

### Features:

- [Bouncy Castle](https://bouncycastle.org/) Security
  Provider <sup>([Configurable](#using-different-security-provider))</sup>

- [AES](./kipher-aes/README.md) (GCM, CCM, CBC, etc.)
- [Message Digests](./kipher-digest/README.md) (MD5, SHA, SHA3, etc.)
- [MACs](./kipher-mac/README.md) (HMAC, etc.)

- and more to be implemented...

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
| ![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-aes?style=flat-square&logo=apachemaven&label=kipher-aes&labelColor=black&color=violet&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-aes)          | Data encryption using AES.                           |
| ![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-digest?style=flat-square&logo=apachemaven&label=kipher-digest&labelColor=black&color=violet&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-digest) | Cryptographic hash functions (SHAs, MD5s, etc.).     |
| ![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-mac?style=flat-square&logo=apachemaven&label=kipher-mac&labelColor=black&color=violet&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-mac)          | Data integrity and authentication using MACs.        |

### Gradle

> **Early Preview:**
>
> Currently only available in [snapshot](#snapshots) version.

```kotlin
implementation("io.github.jhdcruz:kipher-$module:$version") // Replace module & version
```

### Maven

> **Early Preview:**
>
> Currently only available in [snapshot](#snapshots) version.

```xml

<depenedencies>
    <!-- ... -->

    <dependency>
        <groupId>io.github.jhdcruz</groupId>
        <artifactId>kipher-$module</artifactId>  <!-- Replace $module -->
        <version>$version</version>  <!-- Replace $version -->
    </dependency>
</depenedencies>
```

### Snapshots

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
    implementation("io.github.jhdcruz:kipher-aes:1.0.0-SNAPSHOT")
}
```

#### Maven

```xml

<project>
    <!-- ... -->

    <repositories>
        <repository>
            <id>sonatype-snapshots</id>
            <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>

    <dependencies>
        <!-- ... -->

        <dependency>
            <groupId>io.github.jhdcruz</groupId>
            <artifactId>kipher-$module</artifactId>  <!-- Replace $module -->
            <version>$version</version>  <!-- Replace $version -->
        </dependency>
    </dependencies>
</project>
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
        ) // returns Map<String, ByteArray>

        val decrypted = gcmEncryption.decrypt(encrypted)

        // or, individually

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

        // or, individually

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

### Using different security provider

Default security provider is set to [Bouncy Castle](https://bouncycastle.org/).

> **Note**
>
> Changing provider has to be done **before** using any of the library
> functions/methods.

#### Module-specific Provider

##### Kotlin

```kotlin
import io.github.jhdcruz.kipher.aes.AesEncryption
import io.github.jhdcruz.kipher.aes.GcmEncryption

import java.security.Provider
import java.security.Security

class Main {
    fun main() {
        // must be declared only once before using any AES methods 
        // or at the beginning of the app's main method or such.
        val provider: Provider = Security.getProvider("SunJCE")
        AesEncryption.provider = provider

        val encryptionUtils = GcmEncryption()
        // and so on, so forth
    }
}
```

##### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.aes.AesEncryption;
import io.github.jhdcruz.kipher.aes.GcmEncryption;

import java.security.Provider;
import java.security.Security;

// For other JVM-based languages,
// adjust syntax based on language

class Main {
    public static void main(String[] args) {
        // must be declared only once before using any AES methods 
        // or at the beginning of the app's main method or such.
        Provider provider = Security.getProvider("SunJCE");
        AesEncryption.Companion.setProvider(provider);

        GcmEncryption encryptionUtils = GcmEncryption();
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
import io.github.jhdcruz.kipher.common.KipherProvider

import java.security.Provider
import java.security.Security

class Main {
    fun main() {
        // must be declared only once before using any library functions
        // or at the beginning of the app's main method or such.
        val provider: Provider = Security.getProvider("SunJCE")
        KipherProvider.provider = provider

        val encryptionUtils = GcmEncryption()
        // and so on, so forth
    }
}
```

##### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.aes.GcmEncryption;
import io.github.jhdcruz.kipher.common.KipherProvider;

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

> **Warning**
>
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

> **Note**
>
> Each modules are independently versioned to avoid version bumps between unrelated module/s.

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
