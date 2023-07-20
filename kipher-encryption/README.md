# Module kipher-encryption

Library for data encryption.

## Usage

### Using different security provider

```kotlin
import io.github.jhdcruz.kipher.encryption.KipherEncryption

import java.security.Provider
import java.security.Security

class Main {
    fun main() {
        // ! must be declared before using any methods !
        val provider: Provider = Security.getProvider("SunJCE")
        KipherEncryption.provider(provider)

        // ...
    }
}
```
