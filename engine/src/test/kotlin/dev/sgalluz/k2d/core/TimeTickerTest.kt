package dev.sgalluz.k2d.core

import kotlin.test.Test
import kotlin.test.assertEquals

class TimeTickerTest {
    @Test
    fun `ticker should calculate correct delta seconds`() {
        val ticker = TimeTicker()

        // First tick: initial state, should return 0
        val firstDelta = ticker.tick(1_000_000_000L)
        assertEquals(0f, firstDelta)

        // Second tick: 16.66ms later
        val secondDelta = ticker.tick(1_016_666_666L)
        assertEquals(0.016666666f, secondDelta)

        // Third tick: 33.33ms later
        val thirdDelta = ticker.tick(1_050_000_000L)
        assertEquals(0.033333335f, thirdDelta)
    }
}
