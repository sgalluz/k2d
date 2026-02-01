package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import dev.sgalluz.k2d.ecs.systems.FrictionSystem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InputSystemTest {
    private lateinit var world: World

    @BeforeEach
    fun setup() {
        world = World()
    }

    @ParameterizedTest(name = "Direction {0} results in velocity ({1}, {2})")
    @MethodSource("provideDirectionalInput")
    fun `should update Velocity when directional keys are pressed`(
        keyCode: Long,
        expectedX: Float,
        expectedY: Float,
    ) {
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

    @Test
    fun `Player should retain velocity after input is released`() {
        val initialVel = 200f
        val player =
            world.createEntity()
                .add(Velocity(initialVel, 0f))
                .add(PlayerInput())

        // Simulate a frame without pressed keys
        val emptyInputSystem = InputSystem(pressedKeys = mutableListOf())
        val frictionSystem = FrictionSystem(globalFriction = 0.5f)

        emptyInputSystem.update(world.getEntities(), 0.016f)
        frictionSystem.update(world.getEntities(), 0.016f)

        val currentVel = player.get<Velocity>()!!.x
        assertTrue(currentVel > 0f && currentVel < initialVel, "Velocity should decay, not snap to zero")
    }

    @Test
    fun `InputSystem should be reactive on direction change and fluid on release`() {
        val speed = 300f

        val player =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(speed, 0f))
                .add(PlayerInput())

        val inputLeft = InputSystem(mutableListOf(Key.DirectionLeft), speed)
        inputLeft.update(world.getEntities(), 0.016f)
        assertEquals(-speed, player.get<Velocity>()!!.x)

        val inputNone = InputSystem(mutableListOf())
        val friction = FrictionSystem(globalFriction = 5.0f)

        inputNone.update(world.getEntities(), 0.016f)
        friction.update(world.getEntities(), 0.016f)

        val velAfterRelease = player.get<Velocity>()!!.x
        assertTrue(velAfterRelease < 0f && velAfterRelease > -300f, "Deve rallentare fluidamente, non fermarsi a zero")
    }

    @Test
    fun `InputSystem should support custom WASD bindings`() {
        val player = world.createEntity().add(Velocity(0f, 0f)).add(PlayerInput())

        val wasdConfig = InputConfig(bindings = mapOf(InputAction.UP to Key.W))
        val inputSystem = InputSystem(listOf(Key.W), config = wasdConfig)

        inputSystem.update(world.getEntities(), 0.016f)

        assertEquals(-200f, player.get<Velocity>()!!.y, "Player should move UP with W key")
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
