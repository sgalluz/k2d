package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.Entity
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

    @Test
    fun `BoundarySystem should keep entity inside including its width`() {
        val screenWidth = 800f
        val boundarySystem = BoundarySystem(screenWidth, 600f)

        val playerWidth = 50f
        val player = Entity(1)
            .add(Position(screenWidth - 10f, 100f))
            .add(Velocity(100f, 0f))
            .add(BoxCollider(playerWidth, 50f))

        boundarySystem.update(listOf(player), 0.016f)

        val pos = player.get<Position>()!!
        assertTrue(pos.x <= screenWidth - playerWidth, "Entity x (${pos.x}) should be capped at ${screenWidth - playerWidth}")
    }
}