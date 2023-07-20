/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using CCM mode.
 */
class CcmEncryption : AesAEAD(AesModes.AEAD.CCM)
