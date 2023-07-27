/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric.aes

/**
 * AES Encryption using CBC mode with PKCS7 Padding.
 */
class AesCBC7 : AesStandard(AesModes.Standard.CBC7)
