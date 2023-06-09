/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.common

/**
 * Throw custom exception caused by [cause].
 *
 * @constructor creates a new [RuntimeException] with the entire exception
 */
class KipherException : RuntimeException {
    constructor(e: Throwable?) : super(e)
    constructor(message: String) : super(message)
}
