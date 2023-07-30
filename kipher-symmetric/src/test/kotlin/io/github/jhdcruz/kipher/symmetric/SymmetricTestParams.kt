/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.symmetric

import io.github.jhdcruz.kipher.symmetric.aes.AesCBC
import io.github.jhdcruz.kipher.symmetric.aes.AesCBC7
import io.github.jhdcruz.kipher.symmetric.aes.AesCCM
import io.github.jhdcruz.kipher.symmetric.aes.AesCFB
import io.github.jhdcruz.kipher.symmetric.aes.AesCTR
import io.github.jhdcruz.kipher.symmetric.aes.AesEAX
import io.github.jhdcruz.kipher.symmetric.aes.AesGCM
import io.github.jhdcruz.kipher.symmetric.aes.AesGCMSIV
import io.github.jhdcruz.kipher.symmetric.chacha.ChaCha20
import io.github.jhdcruz.kipher.symmetric.chacha.ChaChaPoly
import io.github.jhdcruz.kipher.symmetric.salsa.Salsa20
import io.github.jhdcruz.kipher.symmetric.salsa.XSalsa20
import io.github.jhdcruz.kipher.symmetric.threefish.Threefish1024
import io.github.jhdcruz.kipher.symmetric.threefish.Threefish256
import io.github.jhdcruz.kipher.symmetric.threefish.Threefish512
import java.util.stream.Stream

/**
 * Sample parameters for testing.
 */
internal object SymmetricTestParams {
    val message = "message".encodeToByteArray()
    val tag = "metadata".encodeToByteArray()
    val invalidKey = "invalid-key".encodeToByteArray()

    /**
     * [Stream] collection of Standard encryption.
     */
    @JvmStatic
    val standardClasses: Iterable<Class<out StandardEncryption>> = listOf(
        AesCBC::class.java,
        AesCBC7::class.java,
        AesCFB::class.java,
        AesCTR::class.java,
        ChaCha20::class.java,
        Salsa20::class.java,
        XSalsa20::class.java,
        Threefish256::class.java,
        Threefish512::class.java,
        Threefish1024::class.java,
    )

    /**
     * [Stream] collection of AEAD-based encryption.
     */
    @JvmStatic
    val aeadClasses: Iterable<Class<out AEAD>> = listOf(
        AesEAX::class.java,
        AesGCM::class.java,
        AesGCMSIV::class.java,
        AesCCM::class.java,
        ChaChaPoly::class.java,
    )
}
