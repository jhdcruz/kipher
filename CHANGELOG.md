# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

### Changed

### Removed

### Deprecated

### Removed

### Fixed

### Security

## [1.1.0] - 2023-01-11

### Added

- AES/GCM: Custom key size support `AesGcmEncryption(x)` where `x` is the valid key size in bits.
    - Valid key sizes: `128`, `192`, `256`.
    - Custom key sizes are **optional**, and will default to `256` if not specified.

## [1.0.0] - 2023-01-11

### Added

- Add `@file:JvmSynthetic` to hide `internal` classes from Java

### Changed

- Project structure relayout (composite builds)
- Throw custom exception instead of generic
- **BREAKING CHANGE:** reduce package nesting to `kiper.{module}`

## [0.1.0] - 2023-01-07

### Added

- AES/GCM/NoPadding Encryption (256 bits)
