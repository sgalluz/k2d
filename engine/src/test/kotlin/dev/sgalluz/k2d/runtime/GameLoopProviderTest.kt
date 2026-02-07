package dev.sgalluz.k2d.runtime

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import dev.sgalluz.k2d.rendering.GameCanvasTest
import dev.sgalluz.k2d.runtime.compose.LocalGameLoopClock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

class GameLoopProviderTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `K2DProvideGameLoop provides GameLoopClock via CompositionLocal`() =
        runComposeUiTest {
            val fakeClock = GameCanvasTest.FakeGameLoopClock()
            var resolved: GameLoopClock? = null

            setContent {
                k2dProvideGameLoop(
                    onUpdate = {},
                    gameLoopProvider = { fakeClock },
                ) {
                    resolved = LocalGameLoopClock.current
                }
            }

            runOnIdle {
                assertSame(fakeClock, resolved)
            }
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `frameTick changes are observable`() =
        runComposeUiTest {
            val fakeClock = GameCanvasTest.FakeGameLoopClock()
            var observed = -1L

            setContent {
                k2dProvideGameLoop(
                    onUpdate = {},
                    gameLoopProvider = { fakeClock },
                ) {
                    observed = LocalGameLoopClock.current.frameTick.value
                }
            }

            runOnIdle {
                fakeClock.tick(123L)
            }

            runOnIdle {
                assertEquals(123L, observed)
            }
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `content is executed`() =
        runComposeUiTest {
            var executed = false

            setContent {
                k2dProvideGameLoop(
                    onUpdate = {},
                    gameLoopProvider = { GameCanvasTest.FakeGameLoopClock() },
                ) {
                    executed = true
                }
            }

            runOnIdle {
                assertTrue(executed)
            }
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun `default gameLoopProvider provides a GameLoopClock`() =
        runComposeUiTest {
            var resolved: GameLoopClock? = null

            setContent {
                k2dProvideGameLoop(
                    onUpdate = {},
                ) {
                    resolved = LocalGameLoopClock.current
                }
            }

            runOnIdle {
                assertTrue(resolved != null)
            }
        }
}
