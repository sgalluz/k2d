package dev.sgalluz.k2d.ecs.systems

import dev.sgalluz.k2d.ecs.Friction
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertNull
import kotlin.test.Test

class FrictionSystemTest {
    private lateinit var frictionSystem: FrictionSystem
    private lateinit var world: World

    @BeforeEach
    fun setup() {
        frictionSystem = FrictionSystem(globalFriction = 0.1f)
        world = World()
        world.addSystem(frictionSystem)
    }

    @Test
    fun `should not apply any frictions if the entity has no velocity`() {
        val entity = world.createEntity()
        world.update(1f)
        assertNull(entity.get<Velocity>())
    }

    @Test
    fun `should reduce velocity over time`() {
        val player =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(100f, 0f))

        val iceCube =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(100f, 0f))
                .add(Friction(0.01f)) // near to null friction

        world.update(1f)

        assertEquals(90f, player.get<Velocity>()!!.x, 0.01f)
        assertEquals(99f, iceCube.get<Velocity>()!!.x, 0.01f)
    }
}
