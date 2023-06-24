/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using CBC mode with PKCS7 Padding.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 */
class Cbc7Encryption : BasicEncryption(AesModes.CBC7)
