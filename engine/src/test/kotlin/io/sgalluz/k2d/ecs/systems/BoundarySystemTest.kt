package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Velocity
import io.sgalluz.k2d.ecs.World
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BoundarySystemTest {
    @Test
    fun `boundary system should reverse velocity when hitting limits`() {
        val world = World()
        val entity = world.createEntity()
            .add(Position(801f, 100f)) // Already slightly outside
            .add(Velocity(100f, 0f))

        val boundarySystem = BoundarySystem(width = 800f, height = 600f)
        world.addSystem(boundarySystem)

        world.update(0.1f)

        val vel = entity.get<Velocity>()!!
        val pos = entity.get<Position>()!!

        assertTrue(vel.x < 0, "Velocity x should be negative")
        assertEquals(800f, pos.x, "Position should be snapped to the boundary")
    }
}