/*
 * Copyright 2023 Joshua Hero Dela Cruz
 * SPDX-License-Identifier: Apache-2.0
 */

package kipher.rsa

enum class RsaModes(val mode: String) {
    OAEP_512("RSA/ECB/OAEPWithSHA-512AndMGF1Padding"),
    OAEP_256("RSA/ECB/OAEPWithSHA-256AndMGF1Padding"),
}
