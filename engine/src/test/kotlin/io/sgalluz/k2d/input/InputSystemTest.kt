package io.sgalluz.k2d.input

import io.sgalluz.k2d.ecs.PlayerInput
import io.sgalluz.k2d.ecs.Velocity
import io.sgalluz.k2d.ecs.World
import io.sgalluz.k2d.input.InputSystem
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InputSystemTest {
    @Test
    fun `should update Velocity when keys are pressed`() {
        val world = World()
        val player = world.createEntity()
            .add(Velocity(0f, 0f))
            .add(PlayerInput())
        val pressedKeys = setOf("ArrowRight")
        val inputSystem = InputSystem(pressedKeys)

        inputSystem.update(world.getEntities(), 0.016f)

        val vel = player.get<Velocity>()!!
        assertEquals(200f, vel.x, "Velocity X should be 200 when ArrowRight is pressed")
    }
}