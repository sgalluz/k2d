package dev.sgalluz.k2d.ecs.systems

import dev.sgalluz.k2d.ecs.Friction
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class FrictionSystemTest {
    @Test
    fun `FrictionSystem should reduce velocity over time`() {
        val world = World()
        val frictionSystem = FrictionSystem(globalFriction = 0.1f)
        world.addSystem(frictionSystem)

        val player =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(100f, 0f))

        val iceCube =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(100f, 0f))
                .add(Friction(0.01f)) // near to null friction

        world.update(1f) // 1 second after...

        assertEquals(90f, player.get<Velocity>()!!.x, 0.01f)
        assertEquals(99f, iceCube.get<Velocity>()!!.x, 0.01f)
    }
}
