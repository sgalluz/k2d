package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class InputSystemTest {
    @ParameterizedTest(name = "Direction {0} results in velocity ({1}, {2})")
    @MethodSource("provideDirectionalInput")
    fun `should update Velocity when directional keys are pressed`(
        keyCode: Long,
        expectedX: Float,
        expectedY: Float,
    ) {
        val world = World()
        val player =
            world.createEntity()
                .add(Velocity(0f, 0f))
                .add(PlayerInput())
        val inputSystem = InputSystem(listOf(Key(keyCode)))

        inputSystem.update(world.getEntities(), 0.016f)

        val vel = player.get<Velocity>()!!
        assertEquals(expectedX, vel.x, "Velocity X mismatch")
        assertEquals(expectedY, vel.y, "Velocity Y mismatch")
    }

    @Test
    fun `should handle opposite keys by canceling movement`() {
        val world = World()
        val player =
            world.createEntity()
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

    @Test
    fun `should not crash if PlayerInput component is missing`() {
        val world = World()
        world.createEntity().add(Velocity(0f, 0f))

        val inputSystem = InputSystem(listOf(Key.DirectionRight))

        inputSystem.update(world.getEntities(), 0.016f)
    }

    companion object {
        @JvmStatic
        fun provideDirectionalInput(): List<Arguments> =
            listOf(
                Arguments.of(Key.DirectionRight.keyCode, 200f, 0f),
                Arguments.of(Key.DirectionLeft.keyCode, -200f, 0f),
                Arguments.of(Key.DirectionUp.keyCode, 0f, -200f),
                Arguments.of(Key.DirectionDown.keyCode, 0f, 200f),
            )
    }
}
