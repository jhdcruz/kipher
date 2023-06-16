/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using CFB mode.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 */
class CfbEncryption : BasicEncryption(AesModes.CFB)
