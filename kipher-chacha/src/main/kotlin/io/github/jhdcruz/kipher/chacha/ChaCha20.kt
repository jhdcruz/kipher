/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.chacha

/**
 * Data encryption using ChaCha20
 */
class ChaCha20 : ChaChaEncryption(ChaChaModes.ChaCha20)
