/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

/**
 * AES Encryption using GCM-SIV mode.
 */
class AesGCMSIV : AesAEAD(AesModes.AEAD.GCM_SIV)
