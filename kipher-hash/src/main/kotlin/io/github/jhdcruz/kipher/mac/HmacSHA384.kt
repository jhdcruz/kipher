/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

/**
 * HMAC authentication using SHA-384.
 */
class HmacSHA384 : Hmac(MacModes.HmacSHA384)
