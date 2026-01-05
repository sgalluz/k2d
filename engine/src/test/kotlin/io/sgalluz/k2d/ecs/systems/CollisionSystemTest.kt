package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CollisionSystemTest {

    @Test
    fun `entities with overlapping BoxColliders should be detected`() {
        val world = World()
        val system = CollisionSystem()

        // Entity 1: at (0,0) with size 50x50
        val e1 = world.createEntity()
            .add(Position(0f, 0f))
            .add(BoxCollider(width = 50f, height = 50f))

        // Entity 2: at (40,40) with size 50x50 (Overlaps e1)
        val e2 = world.createEntity()
            .add(Position(40f, 40f))
            .add(BoxCollider(width = 50f, height = 50f))

        system.update(world.getEntities(), 0.016f)

        assertTrue(e1.get<BoxCollider>()!!.isColliding, "Entity 1 should be colliding")
        assertTrue(e2.get<BoxCollider>()!!.isColliding, "Entity 2 should be colliding")
    }

    @Test
    fun `entities far apart should not be detected as colliding`() {
        val world = World()
        val system = CollisionSystem()

        val e1 = world.createEntity()
            .add(Position(0f, 0f))
            .add(BoxCollider(width = 10f, height = 10f))

        val e2 = world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(width = 10f, height = 10f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding, "Entity 1 should not be colliding")
        assertFalse(e2.get<BoxCollider>()!!.isColliding, "Entity 2 should not be colliding")
    }
}