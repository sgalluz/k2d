package io.sgalluz.k2d.ecs

import io.sgalluz.k2d.ecs.systems.MovementSystem
import kotlin.test.Test
import kotlin.test.assertEquals

class MovementSystemTest {

    @Test
    fun `movement system should update position based on velocity`() {
        val world = World()
        val entity = world.createEntity()
            .add(Position(10f, 10f))
            .add(Velocity(100f, 0f)) // Moving right at 100 units/sec

        world.addSystem(MovementSystem())

        // Update for 1 second
        world.update(1.0f)

        val pos = entity.get<Position>()!!
        assertEquals(110f, pos.x, "Entity should have moved 100 units to the right")
        assertEquals(10f, pos.y, "Entity y position should remain unchanged")
    }
}
