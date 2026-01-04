package io.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import io.sgalluz.k2d.ecs.PlayerInput
import io.sgalluz.k2d.ecs.Velocity
import io.sgalluz.k2d.ecs.World
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InputSystemTest {
    @Test
    fun `should update Velocity when keys are pressed`() {
        val world = World()
        val player = world.createEntity()
            .add(Velocity(0f, 0f))
            .add(PlayerInput())
        val pressedKeys = listOf(Key.DirectionRight)
        val inputSystem = InputSystem(pressedKeys)

        inputSystem.update(world.getEntities(), 0.016f)

        val vel = player.get<Velocity>()!!
        assertEquals(200f, vel.x, "Velocity X should be 200 when Key.DirectionRight is pressed")
    }

    @Test
    fun `should handle opposite keys by canceling movement`() {
        val world = World()
        val player = world.createEntity()
            .add(Velocity(0f, 0f))
            .add(PlayerInput())

        val pressedKeys = mutableListOf(Key.DirectionRight, Key.DirectionLeft)
        val inputSystem = InputSystem(pressedKeys)

        inputSystem.update(world.getEntities(), 0.016f)

        val vel = player.get<Velocity>()!!
        assertEquals(0f, vel.x, "Velocity X should be 0 when opposite keys are pressed")
    }

    @Test
    fun `should not crash if Velocity component is missing`() {
        val world = World()
        world.createEntity().add(PlayerInput())

        val inputSystem = InputSystem(listOf(Key.DirectionRight))

        inputSystem.update(world.getEntities(), 0.016f)
    }
}