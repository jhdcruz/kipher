/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

/**
 * AES Encryption using CBC mode using PKCS5 Padding.
 *
 * `PKCS5` and `PKCS7` are [interchangeable](https://stackoverflow.com/a/53139355/16171990) in
 * SunJCE Provider.
 */
class CbcEncryption : AesStandard(AesModes.Basic.CBC)
