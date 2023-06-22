# Module kipher-aes

Library for encrypting/decrypting data using AES encryption algorithm.

## Usage

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
            // optional `key` parameter
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

### Using different security provider

Default security provider is set to [Bouncy Castle](https://bouncycastle.org/).

However it is possible to change the provider to a different one:

```kotlin
import io.github.jhdcruz.kipher.aes.AesEncryption
import io.github.jhdcruz.kipher.aes.GcmEncryption

import java.security.Provider
import java.security.Security

class Main {
    fun main() {
        // ! must be declared before any AES methods !
        val provider: Provider = Security.getProvider("SunJCE")
        AesEncryption.Companion.setProvider(provider)

        val encryptionUtils = GcmEncryption()

        println(encryptionUtils.getCipher().getProvider()) // outputs "SunJCE $version"
    }
}
```

> **Warning**
>
> **This changes the provider for all instances of AES methods.**
>
> There is currently no way to change the provider for a single/specified instance.

### Methods

- `encrypt`
- `decrypt`

Easy and straightforward methods, but relies on internal implementation.

**You cannot decrypt a data that was encrypted by a different method or library**.
Unless they use the same internal implementation as this library.

#### Advanced Usage

- `encryptBare`
- `decryptBare`

Requires all the necessary data for the encryption/decryption process,
such as IV, key, AADs, whatever that is applicable.

**You can decrypt data that was encrypted by a different method or library**.
Unless they involve a different or custom implementation of the
encryption/decryption process.

#### Utilities

| Method    | Description                                                                 |
| --------- | --------------------------------------------------------------------------- |
| `extract` | Get the encryption details from an encrypted data encrypted using kipher.   |
| `concat`  | Concatenates the data, iv, and aad (if applicable) into a single ByteArray. |
