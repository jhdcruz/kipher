/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using CCM mode.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 */
class CcmEncryption : AuthenticatedEncryption(AesModes.CCM)
