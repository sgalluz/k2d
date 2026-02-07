package dev.sgalluz.k2d.input

import dev.sgalluz.k2d.ecs.MouseFollower
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.World
import dev.sgalluz.k2d.input.systems.MouseSystem
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MouseSystemTest {
    private lateinit var world: World

    @BeforeEach
    fun setup() {
        world = World()
    }

    @Test
    fun `should update position for entities with MouseFollower and Position`() {
        val mouseState = MouseState(x = 500f, y = 300f)

        val entity =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(MouseFollower())

        val system = MouseSystem(mouseState)
        system.update(world.getEntities(), 0.016f)

        val pos = entity.get<Position>()
        assertNotNull(pos)
        assertEquals(500f, pos.x)
        assertEquals(300f, pos.y)
    }

    @Test
    fun `should not update position if MouseFollower is missing`() {
        val mouseState = MouseState(x = 100f, y = 200f)

        val entity =
            world.createEntity()
                .add(Position(10f, 20f))

        val system = MouseSystem(mouseState)
        system.update(world.getEntities(), 0.016f)

        val pos = entity.get<Position>()!!
        assertEquals(10f, pos.x)
        assertEquals(20f, pos.y)
    }

    @Test
    fun `should not crash if Position is missing`() {
        val mouseState = MouseState(x = 100f, y = 200f)

        world.createEntity()
            .add(MouseFollower())

        val system = MouseSystem(mouseState)
        system.update(world.getEntities(), 0.016f)
    }

    @Test
    fun `should not crash if entity has neither MouseFollower nor Position`() {
        val mouseState = MouseState(x = 50f, y = 60f)

        world.createEntity()

        val system = MouseSystem(mouseState)
        system.update(world.getEntities(), 0.016f)
    }

    @Test
    fun `should update only entities with MouseFollower among multiple entities`() {
        val mouseState = MouseState(x = 400f, y = 250f)

        val follower =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(MouseFollower())

        val nonFollower =
            world.createEntity()
                .add(Position(10f, 20f))

        val system = MouseSystem(mouseState)
        system.update(world.getEntities(), 0.016f)

        val followerPos = follower.get<Position>()!!
        val nonFollowerPos = nonFollower.get<Position>()!!

        assertEquals(400f, followerPos.x)
        assertEquals(250f, followerPos.y)

        assertEquals(10f, nonFollowerPos.x)
        assertEquals(20f, nonFollowerPos.y)
    }
}
