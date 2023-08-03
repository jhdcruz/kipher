# Module kipher-mac

> [API Documentation](https://kipher-mac.pages.dev)

Library for ensuring data integrity and authenticity using message authentication codes.

## Adding Dependency

![Maven Central](https://img.shields.io/maven-central/v/io.github.jhdcruz/kipher-mac?style=for-the-badge&logo=apachemaven&label=latest&labelColor=black&logoColor=blue&color=blue&link=https%3A%2F%2Fmvnrepository.com%2Fartifact%2Fio.github.jhdcruz%2Fkipher-mac) ![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/io.github.jhdcruz/kipher-mac?server=https%3A%2F%2Fs01.oss.sonatype.org&style=for-the-badge&logo=apachemaven&logoColor=green&label=snapshots&labelColor=black&color=green)

### Gradle

```kotlin
implementation("io.github.jhdcruz:kipher-mac:$version")
```

### Maven

```xml

<dependency>
    <groupId>io.github.jhdcruz</groupId>
    <artifactId>kipher-mac</artifactId>
    <version>$version</version>
</dependency>
```

## Usage

```kotlin
import io.github.jhdcruz.kipher.mac.Mac

// adjust syntax for other JVM languages (ex. java).
fun main() {
    val mac = Mac(MacModes.Poly1305) // replace with desired mode

    val data = "sample data".encodeToByteArray()

    val mac = mac.generateMac(data) // returns Map
    // or,
    val hashString = mac.generateMacString(data) // returns hex string

    // Verifying hashes
    println(mac.verifyMac(data, hash)) // returns true
    // or,
    println(mac.verifyMac(data, hashString)) // returns true
}
```

### Hashing from multiple data

```kotlin
import io.github.jhdcruz.kipher.mac.Mac

// adjust syntax for other JVM languages (ex. java).

class MacTest {

    fun main() {
        val mac = Mac(MacModes.Poly1305) // replace with desired mode

        // accepts iterable data, gets processed in update()
        val dataList: List<ByteArray> = listOf(
            "test".encodeToByteArray(),
            "test2".encodeToByteArray(),
            "test3".encodeToByteArray(),
        )

        val mac = mac.generateMac(data) // returns Map
        // or,
        val hashString = mac.generateMacString(data) // returns hex string

        // Verifying hashes
        println(mac.verifyMac(data, hash)) // returns true
        // or,
        println(mac.verifyMac(data, hashString)) // returns true
    }
}
```
