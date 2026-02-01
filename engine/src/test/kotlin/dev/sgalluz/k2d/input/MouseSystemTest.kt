package dev.sgalluz.k2d.input

import dev.sgalluz.k2d.ecs.MouseFollower
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.World
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class MouseSystemTest {
    @Test
    fun `MouseSystem should update entity position to mouse coordinates`() {
        val world = World()
        val mouseState = MouseState(x = 500f, y = 300f)
        val crosshair = world.createEntity().add(Position(0f, 0f)).add(MouseFollower())

        val mouseSystem = MouseSystem(mouseState)
        mouseSystem.update(world.getEntities(), 0.016f)

        val pos = crosshair.get<Position>()!!
        assertEquals(500f, pos.x)
        assertEquals(300f, pos.y)
    }
}
