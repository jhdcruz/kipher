# Module kipher-core

Core utility libraries used by kipher modules.

> [!NOTE]
>
> Already bundled in all kipher modules.

## Usage

### Changing global provider

If desired, it is also possible to change provider that affects the entire library
modules.

##### Kotlin

```kotlin
import io.github.jhdcruz.kipher.aes.GcmEncryption
import io.github.jhdcruz.kipher.core.KipherProvider

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

### Formatting

- `ByteArray.toHexString()` - Transform a `ByteArray` to a `String` of hexadecimal characters.
- `ByteArray.toBase64()` - Transform a `ByteArray` to a `String` of Base64 characters.
