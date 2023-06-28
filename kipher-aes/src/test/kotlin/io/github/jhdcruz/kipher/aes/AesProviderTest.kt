/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.aes

import org.junit.jupiter.api.parallel.Isolated
import java.security.Security
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertTrue

@Isolated
internal class AesProviderTest {

    @Test
    fun `test custom provider`() {
        AesEncryption.provider = Security.getProvider("SunJCE")

        val currentProvider = AesEncryption.provider.toString()

        assertTrue {
            currentProvider.contains("SunJCE", ignoreCase = true)
        }

        println(currentProvider)
    }

    @AfterTest
    fun `reset provider to default`() {
        AesEncryption.provider = null
    }
}
