/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.aes

/**
 * AES Encryption using CFB mode.
 *
 * To support most use-cases, all returned data are raw [ByteArray]s instead of [String]s.
 *
 * @param keySize Custom key size: `128`, `192`, `256`. (default: `256`)
 */
class CfbEncryption(
    keySize: Int = DEFAULT_KEY_SIZE,
) : BasicEncryption(AesModes.CFB, keySize)
