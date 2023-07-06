/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.mac

/**
 * HMAC authentication using SHA-224.
 */
class HmacSHA224 : Hmac(MacModes.HmacSHA224)
