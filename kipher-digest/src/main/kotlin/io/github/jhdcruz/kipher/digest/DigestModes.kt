/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.jhdcruz.kipher.digest

/**
 * Available message digest [mode]s.
 *
 * Availability of modes depends on the provider.
 */
@Suppress("unused")
enum class DigestModes(val mode: String) {
    BLAKE_2B_160("BLAKE2B-160"),
    BLAKE_2B_256("BLAKE2B-256"),
    BLAKE_2B_384("BLAKE2B-384"),
    BLAKE_2B_512("BLAKE2B-512"),

    BLAKE_2S_128("BLAKE2S-128"),
    BLAKE_2S_160("BLAKE2S-160"),
    BLAKE_2S_224("BLAKE2S-224"),

    BLAKE_3_256("BLAKE3-256"),

    KECCAK_224("Keccak-224"),
    KECCAK_256("Keccak-256"),
    KECCAK_288("Keccak-288"),
    KECCAK_384("Keccak-384"),
    KECCAK_512("Keccak-512"),

    MD5("MD5"),

    RIPEMD128("RIPEMD128"),
    RIPEMD160("RIPEMD160"),
    RIPEMD256("RIPEMD256"),
    RIPEMD320("RIPEMD320"),

    SHA_3_224("SHA3-224"),
    SHA_3_256("SHA3-256"),
    SHA_3_384("SHA3-384"),
    SHA_3_512("SHA3-512"),

    SHA_224("SHA-224"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512"),

    SHAKE_128("SHAKE128"),
    SHAKE_256("SHAKE256"),

    SKEIN_256_128("Skein-256-128"),
    SKEIN_256_160("Skein-256-160"),
    SKEIN_256_224("Skein-256-224"),
    SKEIN_256_256("Skein-256-256"),
    SKEIN_512_128("Skein-512-128"),
    SKEIN_512_160("Skein-512-160"),

    SKEIN_512_224("Skein-512-224"),
    SKEIN_512_256("Skein-512-256"),
    SKEIN_512_384("Skein-512-384"),
    SKEIN_512_512("Skein-512-512"),
    SKEIN_1024_384("Skein-1024-384"),
    SKEIN_1024_512("Skein-1024-512"),
    SKEIN_1024_1024("Skein-1024-1024"),

    SM3("SM3"),

    WHIRLPOOL("Whirlpool"),
}
