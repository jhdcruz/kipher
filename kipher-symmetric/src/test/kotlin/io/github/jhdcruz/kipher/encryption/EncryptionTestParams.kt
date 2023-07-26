/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.encryption

/**
 * Sample parameters for testing.
 */
internal object EncryptionTestParams {
    val message = "message".encodeToByteArray()
    val aad = "metadata".encodeToByteArray()
    val invalidKey = "invalid-key".encodeToByteArray()
}
