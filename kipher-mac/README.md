# Module kipher-mac

Library for ensuring data integrity and authenticity using message authentication codes.

## Adding Dependency

> [!NOTE]
>
> Currently only available in snapshot version.

### Gradle

```kotlin
implementation("io.github.jhdcruz:kipher-mac:0.1.0-SNAPSHOT")
```

### Maven

```xml

<dependency>
    <groupId>io.github.jhdcruz</groupId>
    <artifactId>kipher-mac</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

## Usage

```kotlin
import io.github.jhdcruz.kipher.mac.Mac

// adjust syntax for other JVM languages (ex. java).

class MacTest {

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
