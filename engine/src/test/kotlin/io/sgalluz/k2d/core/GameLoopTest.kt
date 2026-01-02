package io.sgalluz.k2d.core

import kotlin.test.Test
import kotlin.test.assertEquals

class GameLoopTest {

    @Test
    fun `GameLoop should not trigger update on the first frame`() {
        var callCount = 0
        val loop = GameLoop { callCount++ }

        loop.update(1_000_000_000L)

        assertEquals(0, callCount, "Update should not be called on the very first frame")
    }

    @Test
    fun `GameLoop should calculate correct delta time and trigger update`() {
        var receivedDelta = 0f
        val loop = GameLoop { delta -> receivedDelta = delta }

        loop.update(1_000_000_000L) // Frame 1: Init (T=1s)
        loop.update(1_016_666_666L) // Frame 2: after 16.66ms (T=1.016666666s)

        assertEquals(0.016666666f, receivedDelta, "Delta time should match the nanoseconds difference")
        assertEquals(1_016_666_666L, loop.frameState.value, "Internal state for Compose is updated as expected")
    }

    @Test
    fun `GameLoop should accumulate time correctly over multiple frames`() {
        var totalDelta = 0f
        val loop = GameLoop { delta -> totalDelta += delta }

        loop.update(1_000_000_000L) // Start
        loop.update(1_100_000_000L) // +0.1s
        loop.update(1_250_000_000L) // +0.15s

        assertEquals(0.25f, totalDelta, "Total accumulated delta should be 0.25s")
    }
}