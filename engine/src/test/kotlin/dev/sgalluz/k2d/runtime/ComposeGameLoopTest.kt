package dev.sgalluz.k2d.runtime

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runDesktopComposeUiTest
import dev.sgalluz.k2d.core.GameLoop
import dev.sgalluz.k2d.runtime.compose.rememberGameLoop
import dev.sgalluz.k2d.runtime.compose.runGameLoop
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test

class ComposeGameLoopTest {
    @Test
    fun game_loop_updates_and_emits_frame_ticks() =
        runTest {
            val frames = mutableListOf<Long>()
            var updates = 0

            val gameLoop = GameLoop { updates++ }
            val fakeFrames = listOf(10L, 20L, 30L)

            runGameLoop(
                gameLoop = gameLoop,
                emitFrame = { frames += it },
                frameProvider = { onFrame ->
                    fakeFrames.forEach(onFrame)
                },
            )

            assertEquals(listOf(10L, 20L, 30L), frames)
            assertEquals(2, updates)
        }

    // Smoke Test
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
