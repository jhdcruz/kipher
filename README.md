# Enkryption

A simple library helper for encrypting and decrypting data in Java/Kotlin.

The goal of this library is to provide an abstraction layer for encrypting and decrypting data in Java/Kotlin. It is a
helper for encrypting and decrypting data in a simple way.

**Features:**

- AES/GCM/NoPadding > `AesGcmEncryption()`

> **Disclaimer:**
>
> I am not a security expert/guru, this library is primarily made for convenience and productivity.
> If you found a security issue, please see [reporting a security issue](./SECURITY.md).

## Usage

Visit the code documentation [here](https://jhdcruz.github.io/enkryption/).

Import the algorithm you need.

Most encryption has 3 methods/functions:

- `encrypt()`: Encrypts the data.
- `decrypt()`: Decrypts the data.
- `generateKey()`: Generate a key for the encryption.

> Readable documentation is still a work in progress...

## License

This project is licensed under the MIT License - see the [LICENSE](./LICENSE.txt) file for details

## Acknowledgements

- [Java Authenticated Encryption with AES and GCM by *Patrick
  Favre-Bulle*](https://gist.github.com/patrickfav/7e28d4eb4bf500f7ee8012c4a0cf7bbf)
- [Security Best Practices: Symmetric Encryption with AES in Java and Android](https://proandroiddev.com/security-best-practices-symmetric-encryption-with-aes-in-java-7616beaaade9)