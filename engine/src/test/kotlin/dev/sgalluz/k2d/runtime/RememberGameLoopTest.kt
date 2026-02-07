package dev.sgalluz.k2d.runtime

import androidx.compose.ui.test.junit4.createComposeRule
import dev.sgalluz.k2d.runtime.compose.rememberGameLoop
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class RememberGameLoopTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun rememberGameLoop_exposes_clock() {
        var clock: GameLoopClock? = null

        composeRule.setContent {
            clock =
                rememberGameLoop(
                    onUpdate = {},
                    enabled = false,
                )
        }

        composeRule.waitForIdle()

        assertTrue(clock != null)
    }
}
