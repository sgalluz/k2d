package dev.sgalluz.k2d.rendering

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import dev.sgalluz.k2d.runtime.GameLoopClock
import dev.sgalluz.k2d.runtime.compose.LocalGameLoopClock
import kotlin.test.Test
import kotlin.test.assertTrue

class GameCanvasTest {
    class FakeGameLoopClock(initialTick: Long = 0L) : GameLoopClock {
        private val frameState = mutableStateOf(initialTick)
        override val frameTick: State<Long> = frameState

        fun tick(value: Long) {
            frameState.value = value
        }
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun canvas_rerenders_when_clock_ticks() =
        runDesktopComposeUiTest {
            val fakeClock = FakeGameLoopClock()
            var renders = 0

            setContent {
                CompositionLocalProvider(
                    LocalGameLoopClock provides fakeClock,
                ) {
                    k2dCanvas(onRender = { renders++ })
                }
            }

            runOnIdle {
                fakeClock.tick(1)
            }

            runOnIdle {
                assertTrue(renders > 1)
            }
        }
}
