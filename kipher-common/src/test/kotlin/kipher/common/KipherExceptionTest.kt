@file:Suppress("TooGenericExceptionThrown")

package kipher.common

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class KipherExceptionTest {
    @Test
    fun `test throwable exception`() {
        assertThrows<KipherException> {
            throw RuntimeException()
        }
    }

    @Test
    fun `test message exception`() {
        assertThrows<KipherException> {
            throw RuntimeException("Error Thrown")
        }
    }
}
