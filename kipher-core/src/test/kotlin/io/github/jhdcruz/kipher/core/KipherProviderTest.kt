/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.core

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.parallel.Isolated
import java.security.Security
import javax.crypto.Cipher
import kotlin.test.Test
import kotlin.test.assertTrue

@Isolated
internal class KipherProviderTest {

    @Nested
    @Isolated
    @Order(1)
    inner class DefaultProviderTest : KipherProvider() {
        @Test
        fun `test default provider`() {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val currentProvider = cipher.provider.toString()

            assertTrue {
                currentProvider.contains("BC", ignoreCase = true)
            }
        }
    }

    private val customProvider = Security.getProvider("SunJCE")

    @Nested
    @Isolated
    @Order(2)
    inner class CustomProviderTest : KipherProvider(customProvider) {
        @Test
        fun `test different provider`() {
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val currentProvider = cipher.provider.toString()

            assertTrue {
                currentProvider.contains("SunJCE", ignoreCase = true)
            }
        }
    }
}
