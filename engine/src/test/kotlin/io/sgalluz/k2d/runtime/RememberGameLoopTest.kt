package io.sgalluz.k2d.runtime

import androidx.compose.ui.test.junit4.createComposeRule
import io.sgalluz.k2d.runtime.compose.rememberGameLoop
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class RememberGameLoopTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rememberGameLoop_triggers_update() {
        var calls = 0

        composeRule.setContent {
            rememberGameLoop {
                calls++
            }
        }

        composeRule.waitForIdle()

        assertTrue(calls > 0)
    }
}
