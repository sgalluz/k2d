package dev.sgalluz.k2d.runtime

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import dev.sgalluz.k2d.runtime.compose.rememberGameLoop
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test

class ComposeGameLoopSmokeTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun rememberGameLoop_exposes_a_clock() =
        runDesktopComposeUiTest {
            lateinit var clock: GameLoopClock

            setContent {
                clock = rememberGameLoop({}, enabled = false)
            }

            runOnIdle {
                assertNotNull(clock)
            }
        }
}
