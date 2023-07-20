/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using GCM mode.
 */
class GcmEncryption : AesAEAD(AesModes.AEAD.GCM)
