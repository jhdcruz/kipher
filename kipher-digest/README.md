# Module kipher-digest

> [API Documentation](https://kipher-digest.pages.dev)

Library for cryptographic hash functions (such as SHAs, MD5s).

## Adding Dependency

![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-digest?style=for-the-badge&logo=apachemaven&label=latest&labelColor=black&logoColor=blue&color=blue&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-mac) ![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.jhdcruz/kipher-digest?server=https%3A%2F%2Fs01.oss.sonatype.org&style=for-the-badge&logo=apachemaven&logoColor=green&label=snapshots&labelColor=black&color=green)

### Gradle

```kotlin
implementation("io.github.jhdcruz:kipher-digest:$version")
```

### Maven

```xml
<dependency>
    <groupId>io.github.jhdcruz</groupId>
    <artifactId>kipher-digest</artifactId>
    <version>$version</version>
</dependency>
```

## Usage

```kotlin
import io.github.jhdcruz.kipher.digest.Digest

// adjust syntax for other JVM languages (ex. java).

fun main() {
    val digest = Digest(DigestModes.SHA_256) // replace with desired mode

    val data = "sample data".encodeToByteArray()

    val hash = digest.generateHash(data) // returns ByteArray
    // or,
    val hashString = digest.generateHashString(data) // returns hex string

    // Verifying hashes
    println(digest.verifyHash(data, hash)) // returns true
    // or,
    println(digest.verifyHash(data, hashString)) // returns true
}
```

### Hashing from multiple data

```kotlin
import io.github.jhdcruz.kipher.digest.Digest

// adjust syntax for other JVM languages (ex. java).

class DigestTest {

    fun main() {
        val digest = Digest(DigestModes.SHA_256) // replace with desired mode

        // accepts iterable data, gets processed in update()
        val dataList: List<ByteArray> = listOf(
            "test".encodeToByteArray(),
            "test2".encodeToByteArray(),
            "test3".encodeToByteArray(),
        )

        val hash = digest.generateHash(data) // returns ByteArray
        // or,
        val hashString = digest.generateHashString(data) // returns hex string

        // Verifying hashes
        println(digest.verifyHash(data, hash)) // returns true
        // or,
        println(digest.verifyHash(data, hashString)) // returns true
    }
}
```
