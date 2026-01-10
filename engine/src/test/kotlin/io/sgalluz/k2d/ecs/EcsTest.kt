package io.sgalluz.k2d.ecs

import io.sgalluz.k2d.ecs.systems.GameSystem
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class EcsTest {

    @Test
    fun `entities should store and retrieve components`() {
        val entity = Entity(id = 1)
        val position = Position(x = 10f, y = 20f)

        entity.add(position)
        val retrieved = entity.get<Position>()

        assertNotNull(retrieved)
        assertEquals(10f, retrieved.x)
    }

    @Test
    fun `world should update systems`() {
        val world = World()
        var systemCalled = false

        val testSystem = object : GameSystem {
            // Add the entities parameter here to respect the interface
            override fun update(entities: List<Entity>, deltaTime: Float) {
                systemCalled = true
            }
        }

        world.addSystem(testSystem)
        world.update(0.16f)

        assertEquals(true, systemCalled, "System should be updated by the world")
    }

    @Test
    fun `entities with DeletionMark should be removed from world at the end of update`() {
        val world = World()
        val entity = world.createEntity().add(Position(0f, 0f))
        entity.add(DeletionMark())

        world.update(0.016f)

        assertFalse(world.getEntities().contains(entity), "Entity should be removed from the world")
    }
}