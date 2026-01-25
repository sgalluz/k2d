package dev.sgalluz.k2d.core

import kotlin.test.Test
import kotlin.test.assertTrue

class GameLoopTest {
    @Test
    fun `update calls onUpdate with positive delta`() {
        val deltas = mutableListOf<Float>()

        val gameLoop =
            GameLoop { delta ->
                deltas += delta
            }

        gameLoop.update(1_000_000L)
        gameLoop.update(2_000_000L)

        assertTrue(deltas.isNotEmpty())
        assertTrue(deltas.all { it > 0f })
    }
}
