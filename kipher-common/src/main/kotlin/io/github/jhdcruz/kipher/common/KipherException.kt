/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.common

/**
 * Throw custom exception caused by [cause].
 *
 * @constructor creates a new [Exception] with the entire exception
 */
class KipherException : Exception {
    constructor(e: Throwable?) : super(e)
    constructor(message: String, e: Throwable?) : super(message, e)
}
