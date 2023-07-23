/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using CBC mode with PKCS7 Padding.
 */
class Cbc7Encryption : AesStandard(AesModes.Basic.CBC7)
