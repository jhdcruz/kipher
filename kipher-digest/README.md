# Module kipher-digest

Library for cryptographic hash functions (such as SHAs, MD5s).

## Adding Dependency

> **Note**
>
> Currently only available in snapshot version.

### Gradle

```kotlin
implementation("io.github.jhdcruz:kipher-digest:0.1.0-SNAPSHOT")
```

### Maven

```xml

<dependency>
    <groupId>io.github.jhdcruz</groupId>
    <artifactId>kipher-digest</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

```kotlin
import io.github.jhdcruz.kipher.digest.Digest

// adjust syntax for other JVM languages (ex. java).

class DigestTest {

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
