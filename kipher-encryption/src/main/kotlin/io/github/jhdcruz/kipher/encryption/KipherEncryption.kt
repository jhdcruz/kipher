/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.encryption

import io.github.jhdcruz.kipher.common.KipherProvider
import org.jetbrains.annotations.Nullable
import java.security.Provider
import java.security.SecureRandom

open class KipherEncryption(
    @Nullable val provider: Provider? = null,
) : KipherProvider(defaultProvider) {
    internal val randomize = SecureRandom()

    companion object {
        /**
         * JCE provider to use for encryption.
         */
        var defaultProvider: Provider? = provider
    }
}
