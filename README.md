# aes-gcm-encryption

A simple library for encrypting and decrypting data using AES/GCM/NoPadding.

## Usage

Using the library is as simple as importing (.jar) and instantiating it.

The library only has 3 methods:

- `encrypt` - Encrypts a string
- `decrypt` - Decrypts a string
- `generateSecretKey` - Generates a secret key (for decryption)

> **Java/JVM 17+ is required to use this library.**

### Encrypting

- Java:

  ```java
  // ...
  String data = "sample data";
  bytes[] secretKey = encryptionUtils.generateSecretKey();
  
  bytes[] encryptedData = new EncryptionUtils.encrypt(data, secretKey)
  ```

- Kotlin:

  ```kotlin
  // ...
  val data = "sample data"
  val secretKey = encryptionUtils.generateSecretKey()
  
  val encryptedData = encryptionUtils.encrypt(data, secretKey)
  ```

## Decrypting

- Java:

  ```java
  // ...
  bytes[] secretKey = resultSet.getBytes("secret_key");
  bytes[] passwordHash = resultSet.getBytes("password");
  
  String decryptedPass = new EncryptionUtils.decrypt(passwordHash, secretKey);
  // ...
  ```

- Kotlin:

  ```kotlin
  // ...
  val secretKey: ByteArray = resultSet.getBytes("secret_key")
  val passwordHash: ByteArray = resultSet.getBytes("password")
  
  val decryptedPass = encryptionUtils.decrypt(passwordHash, secretKey)
  // ...
  ```

## Acknowledgements

- [Java Authenticated Encryption with AES and GCM by *Patrick
  Favre-Bulle*](https://gist.github.com/patrickfav/6e28d4eb4bf500f7ee8012c4a0cf7bbf)