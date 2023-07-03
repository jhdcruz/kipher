/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.hash.mac

/**
 * HMAC authentication using SHA-512.
 */
class HmacSHA512 : Hmac(MacModes.HmacSHA512)
