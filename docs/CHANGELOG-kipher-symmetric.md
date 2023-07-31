# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- HMAC Support

### Changed

### Removed

### Deprecated

### Fixed

### Security

## [0.1.1] - 2023-07-31

### Fixed

- fix dependency `kipher-core` set to unpublished snapshot.

## [0.1.0] - 2023-07-31

### Added

- Initial usable release

## [0.8.0] - 2023-05-27

### Added

- runtime JARs (`*-compat.jar`)
    - For projects that are non-kotlin _(ex. Java projects)_

- Brought back other modes for AES encryption
    - `CbcEncryption()`
    - `CfbEncryption()`
    - `CtrEncryption()`
    - `CcmEncryption()` _(CCM is a variant of GCM)_

### Fixed

- Fixed missing dependencies errors
    - Such as: `Missing BaseEncryption()`, `Missing Bouncy Castle`

### Security

## [0.7.0] - 2023-04-21

### Added

- **BREAKING CHANGE:** Security provider now defaults to bouncy castle

- **BREAKING CHANGE:** `GcmEncryption()` and `CbcEncryption()` replaces `KipherAes()`

- Introduces `encryptWithIv()` which returns both the encrypted
  data and iv in `Pair`. Applicable for `GcmEncryption()` and `CbcEncryption()`.
    - Add a new `iv` parameter to `decrypt()` for separate iv input.

### Changed

- Refactor interfaces to abstract/sealed classes

### Removed

- Removed `KipherAes()`
    - See `GcmEncryption()` and `CbcEncryption()` for replacement

## [0.6.0]

### Changed

- BREAKING CHANGE: `AesGcmEncryption()` is no longer available. Use `AesEncryption()` instead.

## [0.5.0]

### Deprecated

- Deprecate obsolete aes modes:
    - `AES/CTR/NoPadding`
    - `"AES/CFB/NoPadding`
    - `AES/OFB/NoPadding`

## [0.4.0] - 2023-01-24

### Added

- Introduce `AesEncryption()` to support other AES encryption modes:
    - `AES/CBC/PKCS5Padding`
    - `AES/CTR/NoPadding`
    - `"AES/CFB/NoPadding`
    - `AES/OFB/NoPadding`

### Changed

- `AesGcmEncryption()` was renamed to `AesEncryption()`

### Removed

- BREAKING CHANGE: `AesGcmEncryption()` is no longer available. Use `AesEncryption()` instead.

## [0.3.0] - 2023-01-11

### Added

- AES/GCM: Custom key size support `AesGcmEncryption(x)` where `x` is the valid key size in bits.
    - Valid key sizes: `128`, `192`, `256`.
    - Custom key sizes are **optional**, and will default to `256` if not specified.

## [0.2.0] - 2023-01-11

### Added

- Add `@file:JvmSynthetic` to hide `internal` classes from Java

### Changed

- Project structure relayout (composite builds)
- Throw custom exception instead of generic
- **BREAKING CHANGE:** reduce package nesting to `kiper.{module}`

## [0.1.0] - 2023-01-07

### Added

- AES/GCM/NoPadding Encryption (256 bits)
