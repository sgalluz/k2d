package dev.sgalluz.k2d.rendering

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runDesktopComposeUiTest
import androidx.compose.ui.unit.dp
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

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun canvas_is_present_and_measured() =
        runDesktopComposeUiTest {
            setContent {
                CompositionLocalProvider(
                    LocalGameLoopClock provides FakeGameLoopClock(),
                ) {
                    k2dCanvas(onRender = {})
                }
            }

            onNodeWithTag("k2d-canvas")
                .assertExists()
                .assertIsDisplayed()
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun canvas_fills_parent() =
        runDesktopComposeUiTest {
            setContent {
                CompositionLocalProvider(
                    LocalGameLoopClock provides FakeGameLoopClock(),
                ) {
                    Box(Modifier.size(200.dp)) {
                        k2dCanvas(
                            modifier = Modifier.testTag("k2d-canvas"),
                            onRender = {},
                        )
                    }
                }
            }

            onNodeWithTag("k2d-canvas")
                .assertWidthIsEqualTo(200.dp)
                .assertHeightIsEqualTo(200.dp)
        }
}
