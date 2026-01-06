package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class CollisionSystemTest {

    @Test
    fun `entities with overlapping BoxColliders should be detected`() {
        val world = World()
        val system = CollisionSystem()

        val e1 = world.createEntity()
            .add(Position(0f, 0f))
            .add(BoxCollider(width = 50f, height = 50f))

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

        val e1 = world.createEntity().add(Position(0f, 0f)).add(BoxCollider(10f, 10f))
        val e2 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(10f, 10f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is to the LEFT of source`() {
        val world = World()
        val system = CollisionSystem()
        // e1 at (100, 100), e2 far to the left at (0, 100)
        val e1 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(0f, 100f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is to the RIGHT of source`() {
        val world = World()
        val system = CollisionSystem()
        // e1 at (0, 0), e2 far to the right at (100, 0)
        val e1 = world.createEntity().add(Position(0f, 0f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(100f, 0f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is ABOVE source`() {
        val world = World()
        val system = CollisionSystem()
        // e1 at (100, 100), e2 far above at (100, 0)
        val e1 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(100f, 0f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is BELOW source`() {
        val world = World()
        val system = CollisionSystem()
        // e1 at (0, 0), e2 far below at (0, 100)
        val e1 = world.createEntity().add(Position(0f, 0f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(0f, 100f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should ignore entities missing Position or BoxCollider`() {
        val world = World()
        val system = CollisionSystem()

        world.createEntity().add(Position(0f, 0f)) // Only Position
        world.createEntity().add(BoxCollider(50f, 50f)) // Only Collider

        val e3 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(10f, 10f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e3.get<BoxCollider>()!!.isColliding, "System should still run correctly for valid entities")
    }

    @Test
    fun `should reset isColliding even for entities without Position`() {
        val world = World()
        val system = CollisionSystem()

        val collider = BoxCollider(50f, 50f, isColliding = true)
        world.createEntity().add(collider) // Missing Position, but should be reset

        system.update(world.getEntities(), 0.016f)

        assertFalse(collider.isColliding, "isColliding should be reset to false during update")
    }

    @Test
    fun `should reset isColliding to false when entities move apart`() {
        val world = World()
        val system = CollisionSystem()
        val collider = BoxCollider(50f, 50f, isColliding = true)

        world.createEntity().add(Position(0f, 0f)).add(collider)
        world.createEntity().add(Position(200f, 200f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(collider.isColliding, "Should reset to false if no longer touching")
    }

    @Test
    fun `static resolution should push mobile entity out of static entity from the left`() {
        val world = World()
        val system = CollisionSystem()

        val wall = world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, isStatic = true))

        val player = world.createEntity()
            .add(Position(60f, 100f))
            .add(BoxCollider(50f, 50f, isStatic = false))

        system.update(world.getEntities(), 0.016f)

        val playerPos = player.get<Position>()!!

        assertEquals(50f, playerPos.x, "Player should be pushed out to X=50")
    }

    @Test
    fun `static resolution should push mobile entity out of static entity from the right`() {
        val world = World()
        val system = CollisionSystem()

        val wall = world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, isStatic = true))

        val player = world.createEntity()
            .add(Position(140f, 100f))
            .add(BoxCollider(50f, 50f, isStatic = false))

        system.update(world.getEntities(), 0.016f)

        assertEquals(150f, player.get<Position>()!!.x, "Should be pushed to the right edge of the wall")
    }

    @Test
    fun `static resolution should push mobile entity out of static entity from the top`() {
        val world = World()
        val system = CollisionSystem()

        val wall = world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, isStatic = true))

        val player = world.createEntity()
            .add(Position(100f, 60f))
            .add(BoxCollider(50f, 50f, isStatic = false))

        system.update(world.getEntities(), 0.016f)

        assertEquals(50f, player.get<Position>()!!.y, "Should be pushed to the top edge of the wall")
    }

    @Test
    fun `static resolution should push mobile entity out of static entity from the bottom`() {
        val world = World()
        val system = CollisionSystem()

        val wall = world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, isStatic = true))

        val player = world.createEntity()
            .add(Position(100f, 140f))
            .add(BoxCollider(50f, 50f, isStatic = false))

        system.update(world.getEntities(), 0.016f)

        assertEquals(150f, player.get<Position>()!!.y, "Should be pushed to the bottom edge of the wall")
    }

    @Test
    fun `should resolve collisions with multiple static entities (corner case)`() {
        val world = World()
        val system = CollisionSystem()

        // Horizontal Wall (Floor)
        world.createEntity()
            .add(Position(0f, 100f))
            .add(BoxCollider(width = 200f, height = 50f, isStatic = true))

        // Vertical Wall (Right Wall)
        world.createEntity()
            .add(Position(150f, 0f))
            .add(BoxCollider(width = 50f, height = 150f, isStatic = true))

        // Player (50x50) stuck in the corner
        // Position (120, 80) -> Pass through the floor (Y) and wall (X)
        val player = world.createEntity()
            .add(Position(120f, 80f))
            .add(BoxCollider(width = 50f, height = 50f, isStatic = false))

        system.update(world.getEntities(), 0.016f)

        val pos = player.get<Position>()!!

        // It must be pushed to the left of the vertical wall (X <= 100 because 100 + 50 = 150)
        // and above the horizontal wall (Y <= 50 because 50 + 50 = 100)
        assertEquals(100f, pos.x, "Should be pushed out of the vertical wall")
        assertEquals(50f, pos.y, "Should be pushed out of the horizontal wall")
    }
}