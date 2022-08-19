/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package utilities

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.assertNotNull

class LibraryTest {
    @Test fun someLibraryMethodReturnsTrue() {
        assert(UtilitiesLibrary.randomCode(10).length == 10)

        assertFails {
            UtilitiesLibrary.Companion.ValueClasses.Email("notEmail@string@")
        }
        assertNotNull(UtilitiesLibrary.Companion.ValueClasses.Email("email@string.ru").email)
    }
}
