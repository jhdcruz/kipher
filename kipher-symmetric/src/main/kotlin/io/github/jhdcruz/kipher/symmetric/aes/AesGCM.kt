/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

/**
 * AES Encryption using GCM mode.
 */
class AesGCM : AesAEAD(AesModes.AEAD.GCM)
