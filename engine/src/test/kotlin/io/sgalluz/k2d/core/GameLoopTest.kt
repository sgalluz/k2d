package io.sgalluz.k2d.core

import kotlin.test.Test
import kotlin.test.assertEquals

class GameLoopTest {

    @Test
    fun `test movement logic with delta time`() {
        var position = 0f
        val speed = 100f // 100 pixels per second
        val deltaTime = 0.5f // half a second passed

        position += speed * deltaTime

        assertEquals(50f, position, "Position should be 50 after 0.5 seconds")
    }
}