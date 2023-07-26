# Module kipher-symmetric

Library for data encryption using symmetric encryption algorithms.

## Symmetric Ciphers

Data encryption example using AES:

### Kotlin

```kotlin
import io.github.jhdcruz.kipher.symmetric.aes.AesGcm

class EncryptionTest {

    fun main() {
        val encryptionUtils = AesGcm()

        val data = "sample data".encodeToByteArray()
        val aad = "sample aad".encodeToByteArray()

        // named parameters are recommended, but optional
        val encrypted = AesGcm.encrypt(
            data = message,
            aad = aad,
            // optional `key` parameter
        ) // returns Map<String, ByteArray> of [data, key]

        val decrypted = AesGcm.decrypt(encrypted)

        // or

        val decrypted = AesGcm.decrypt(
            encrypted = encrypted.getValue("data"),
            key = encrypted.getValue("key")
        )

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

### Java (Non-kotlin)

```java
import io.github.jhdcruz.kipher.symmetric.aes.AesGcm;

import java.util.Map;

public class Main {

    public static void main(String[] args) {
        AesGcm encryptionUtils = new AesGcm();

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
import io.github.jhdcruz.kipher.symmetric.aes.AesCbc

class EncryptionTest {

    fun main() {
        val encryptionUtils = AesCbc()

        val data = "sample data".encodeToByteArray()

        val secretKey: ByteArray = encryptionUtils.generateKey(128) // should be a valid one

        val encrypted = encryptionUtils.encrypt(
            data = message,
            key = secretKey
        )

        val decrypted = encryptionUtils.decrypt(encrypted)

        println(decryptedPass.toString(), Charsets.UTF_8) // outputs "sample data"
    }
}
```

### Using different security provider

```kotlin
import io.github.jhdcruz.kipher.symmetric.SymmetricEncryption

import java.security.Provider
import java.security.Security

class Main {
    fun main() {
        // must be declared before using any symmetric ciphers methods!
        val provider: Provider = Security.getProvider("SunJCE")
        SymmetricEncryption.provider(provider)
    }
}
```

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
|-----------|-----------------------------------------------------------------------------|
| `extract` | Get the encryption details from an encrypted data encrypted using kipher.   |
| `concat`  | Concatenates the data, iv, and aad (if applicable) into a single ByteArray. |
