package dev.sgalluz.k2d.ecs.systems

import dev.sgalluz.k2d.ecs.BoxCollider
import dev.sgalluz.k2d.ecs.CollisionResponse
import dev.sgalluz.k2d.ecs.DeletionMark
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollisionSystemTest {
    private lateinit var world: World
    private lateinit var system: CollisionSystem

    @BeforeEach
    fun setup() {
        world = World()
        system = CollisionSystem()
    }

    @Test
    fun `entities with overlapping BoxColliders should be detected`() {
        val e1 =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(BoxCollider(width = 50f, height = 50f))

        val e2 =
            world.createEntity()
                .add(Position(40f, 40f))
                .add(BoxCollider(width = 50f, height = 50f))

        system.update(world.getEntities(), 0.016f)

        assertTrue(e1.get<BoxCollider>()!!.isColliding, "Entity 1 should be colliding")
        assertTrue(e2.get<BoxCollider>()!!.isColliding, "Entity 2 should be colliding")
    }

    @Test
    fun `entities far apart should not be detected as colliding`() {
        val e1 = world.createEntity().add(Position(0f, 0f)).add(BoxCollider(10f, 10f))
        val e2 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(10f, 10f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is to the LEFT of source`() {
        // e1 at (100, 100), e2 far to the left at (0, 100)
        val e1 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(0f, 100f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is to the RIGHT of source`() {
        // e1 at (0, 0), e2 far to the right at (100, 0)
        val e1 = world.createEntity().add(Position(0f, 0f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(100f, 0f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is ABOVE source`() {
        // e1 at (100, 100), e2 far above at (100, 0)
        val e1 = world.createEntity().add(Position(100f, 100f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(100f, 0f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should not collide when target is BELOW source`() {
        // e1 at (0, 0), e2 far below at (0, 100)
        val e1 = world.createEntity().add(Position(0f, 0f)).add(BoxCollider(50f, 50f))
        val e2 = world.createEntity().add(Position(0f, 100f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e1.get<BoxCollider>()!!.isColliding)
        assertFalse(e2.get<BoxCollider>()!!.isColliding)
    }

    @Test
    fun `should ignore entities missing Position or BoxCollider`() {
        world.createEntity().add(Position(0f, 0f)) // Only Position
        world.createEntity().add(BoxCollider(50f, 50f)) // Only Collider
        val e3 =
            world.createEntity()
                .add(Position(100f, 100f))
                .add(BoxCollider(10f, 10f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(e3.get<BoxCollider>()!!.isColliding, "System should still run correctly for valid entities")
    }

    @Test
    fun `should reset isColliding even for entities without Position`() {
        val collider = BoxCollider(50f, 50f, isColliding = true)
        world.createEntity().add(collider) // Missing Position, but should be reset

        system.update(world.getEntities(), 0.016f)

        assertFalse(collider.isColliding, "isColliding should be reset to false during update")
    }

    @Test
    fun `should reset isColliding to false when entities move apart`() {
        val collider = BoxCollider(50f, 50f, isColliding = true)

        world.createEntity().add(Position(0f, 0f)).add(collider)
        world.createEntity().add(Position(200f, 200f)).add(BoxCollider(50f, 50f))

        system.update(world.getEntities(), 0.016f)

        assertFalse(collider.isColliding, "Should reset to false if no longer touching")
    }

    @ParameterizedTest(name = "Push out from {0}: player at ({1}, {2}) should be moved to ({3}, {4})")
    @MethodSource("provideStaticResolutionScenarios")
    fun `static resolution should push mobile entity out of static entity correctly`(
        direction: String,
        initialX: Float,
        initialY: Float,
        expectedX: Float,
        expectedY: Float,
    ) {
        // Wall fixed at (100, 100) with size 50x50
        world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.STATIC))

        // Player with size 50x50 at variable initial position
        val player =
            world.createEntity()
                .add(Position(initialX, initialY))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.NONE))

        system.update(world.getEntities(), 0.016f)

        val pos = player.get<Position>()!!
        assertEquals(expectedX, pos.x, "X mismatch when pushing from $direction")
        assertEquals(expectedY, pos.y, "Y mismatch when pushing from $direction")
    }

    companion object {
        @JvmStatic
        fun provideStaticResolutionScenarios() =
            listOf(
                // Arguments: Direction, InitialX, InitialY, ExpectedX, ExpectedY
                Arguments.of("LEFT", 60f, 100f, 50f, 100f),
                Arguments.of("RIGHT", 140f, 100f, 150f, 100f),
                Arguments.of("TOP", 100f, 60f, 100f, 50f),
                Arguments.of("BOTTOM", 100f, 140f, 100f, 150f),
            )
    }

    @Test
    fun `should resolve collisions with multiple static entities (corner case)`() {
        // Arrange
        world.createEntity() // Horizontal Wall (Floor)
            .add(Position(0f, 100f))
            .add(BoxCollider(width = 200f, height = 50f, response = CollisionResponse.STATIC))

        world.createEntity() // Vertical Wall (Right Wall)
            .add(Position(150f, 0f))
            .add(BoxCollider(width = 50f, height = 150f, response = CollisionResponse.STATIC))

        // Player (50x50) stuck in the corner
        // Position (120, 80) -> Pass through the floor (Y) and wall (X)
        val player =
            world.createEntity()
                .add(Position(120f, 80f))
                .add(BoxCollider(width = 50f, height = 50f))

        // Act
        system.update(world.getEntities(), 0.016f)

        // Assert
        val pos = player.get<Position>()!!

        // It must be pushed to the left of the vertical wall (X <= 100 because 100 + 50 = 150)
        // and above the horizontal wall (Y <= 50 because 50 + 50 = 100)
        assertEquals(100f, pos.x, "Should be pushed out of the vertical wall")
        assertEquals(50f, pos.y, "Should be pushed out of the horizontal wall")
    }

    @Test
    fun `dynamic resolution should make entities bounce (swap velocities)`() {
        // Arrange
        val e1 =
            world.createEntity()
                .add(Position(40f, 100f))
                .add(Velocity(100f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        val e2 =
            world.createEntity()
                .add(Position(60f, 100f))
                .add(Velocity(-100f, 0f)) // imminent collision (30px overlap)
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        // Act
        system.update(world.getEntities(), 0.016f)

        // Assert
        assertTrue(e1.get<Velocity>()!!.x < 0, "Entity 1 should now move left")
        assertTrue(e2.get<Velocity>()!!.x > 0, "Entity 2 should now move right")

        val dist = abs(e1.get<Position>()!!.x - e2.get<Position>()!!.x)
        assertTrue(dist >= 50f, "Entities should no longer overlap after bounce resolution")
    }

    @Test
    fun `entities with response NONE should detect collision but not move`() {
        val e1 =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.NONE))

        world.createEntity()
            .add(Position(10f, 0f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.NONE))

        val initialPos1 = e1.get<Position>()!!.copy()

        system.update(world.getEntities(), 0.016f)

        assertTrue(e1.get<BoxCollider>()!!.isColliding)
        assertEquals(initialPos1.x, e1.get<Position>()!!.x, "Entity with NONE should not move")
    }

    @Test
    fun `bounce resolution should handle vertical collisions`() {
        val e1 =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(0f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        val e2 =
            world.createEntity()
                .add(Position(0f, 40f))
                .add(Velocity(0f, -100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        system.update(world.getEntities(), 0.016f)

        assertTrue(e1.get<Velocity>()!!.y < 0, "E1 should bounce UP")
        assertTrue(e2.get<Velocity>()!!.y > 0, "E2 should bounce DOWN")
    }

    @Test
    fun `BOUNCE entity should bounce off STATIC entity`() {
        // wall
        world.createEntity()
            .add(Position(100f, 0f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.STATIC))

        val ball =
            world.createEntity()
                .add(Position(60f, 0f))
                .add(Velocity(100f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        system.update(world.getEntities(), 0.016f)

        assertTrue(ball.get<Velocity>()!!.x < 0, "Ball should have reversed its X velocity after hitting the wall")
        assertEquals(50f, ball.get<Position>()!!.x, "Ball should have been pushed out of the wall")
    }

    @Test
    fun `PUSH vs PUSH on X axis should resolve position but maintain velocity`() {
        val e1 =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(100f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.PUSH))

        val e2 =
            world.createEntity()
                .add(Position(40f, 0f))
                .add(Velocity(0f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.PUSH))

        system.update(world.getEntities(), 0.016f)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!
        assertTrue(abs(p1.x - p2.x) >= 50f, "Entities should be separated")
        assertEquals(100f, e1.get<Velocity>()!!.x, "E1 should maintain velocity")
        assertEquals(0f, e2.get<Velocity>()!!.x, "E2 should maintain velocity")
    }

    @Test
    fun `PUSH vs PUSH on Y axis should resolve position but maintain velocity`() {
        val e1 =
            world.createEntity()
                .add(Position(0f, 0f))
                .add(Velocity(0f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.PUSH))

        val e2 =
            world.createEntity()
                .add(Position(0f, 40f))
                .add(Velocity(0f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.PUSH))

        system.update(world.getEntities(), 0.016f)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!

        assertTrue(abs(p1.y - p2.y) >= 50f, "Entities should be separated")
        assertEquals(100f, e1.get<Velocity>()!!.y, "E1 should maintain velocity")
        assertEquals(0f, e2.get<Velocity>()!!.y, "E2 should maintain velocity")
    }

    @Test
    fun `BOUNCE vs STATIC should resolve position and reverse velocity`() {
        // wall
        world.createEntity()
            .add(Position(100f, 0f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.STATIC))

        val ball =
            world.createEntity()
                .add(Position(60f, 0f))
                .add(Velocity(100f, 0f)) // Va verso destra (muro)
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        system.update(world.getEntities(), 0.016f)

        assertEquals(50f, ball.get<Position>()!!.x, "Ball should be pushed out to X=50")
        assertTrue(ball.get<Velocity>()!!.x < 0, "Ball velocity should be reversed")
    }

    @Test
    fun `EXPLODE vs STATIC should mark bomb for deletion`() {
        world.addSystem(CollisionSystem())

        // wall
        world.createEntity()
            .add(Position(140f, 100f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.STATIC))

        val bomb =
            world.createEntity()
                .add(Position(100f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        world.update(0.016f)

        assertTrue(bomb.has<DeletionMark>(), "The bomb should have the DeletionMark after the collision")
    }

    @Test
    fun `EXPLODE vs BOUNCE should push the victim away`() {
        world.addSystem(CollisionSystem())

        // bomb
        world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        val player =
            world.createEntity()
                .add(Position(140f, 100f))
                .add(Velocity(0f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        world.update(0.016f)

        val velocity = player.get<Velocity>()!!
        assertTrue(velocity.x > 0f, "The player should have a positive velocity on X axis")
    }

    @Test
    fun `EXPLODE vs BOUNCE on Y axis should push the victim vertically`() {
        world.addSystem(CollisionSystem())

        // bomb
        world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        val player =
            world.createEntity()
                .add(Position(100f, 140f))
                .add(Velocity(0f, 0f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        world.update(0.016f)

        val velocity = player.get<Velocity>()!!
        assertTrue(velocity.y > 0f, "The player should have a positive velocity on Y axis")
    }

    @Test
    fun `EXPLODE vs EXPLODE should mark both bombs for deletion`() {
        world.addSystem(CollisionSystem())

        val bombA =
            world.createEntity()
                .add(Position(100f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        val bombB =
            world.createEntity()
                .add(Position(140f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        world.update(0.016f)

        assertTrue(bombA.has<DeletionMark>(), "Bomb A should be marked for deletion")
        assertTrue(bombB.has<DeletionMark>(), "Bomb B should be marked for deletion")
    }

    @Test
    fun `EXPLODE vs STATIC should destroy bomb and not move the static wall`() {
        world.addSystem(CollisionSystem())

        val bomb =
            world.createEntity()
                .add(Position(100f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        val wall =
            world.createEntity()
                .add(Position(140f, 100f))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.STATIC))

        world.update(0.016f)

        assertTrue(bomb.has<DeletionMark>(), "Bomb must explode when hit the wall")
        assertFalse(wall.has<Velocity>(), "The wall won't gain any Velocity from the collision")
    }

    @Test
    fun `EXPLODE should push victim without cancelling existing velocity on the other axis`() {
        world.addSystem(CollisionSystem())

        // bomb
        world.createEntity()
            .add(Position(100f, 100f))
            .add(BoxCollider(50f, 50f, response = CollisionResponse.EXPLODE))

        val initialYVelocity = -200f
        val player =
            world.createEntity()
                .add(Position(140f, 100f))
                .add(Velocity(0f, initialYVelocity))
                .add(BoxCollider(50f, 50f, response = CollisionResponse.BOUNCE))

        world.update(0.016f)

        val velocity = player.get<Velocity>()!!
        assertTrue(velocity.x > 0f, "The player should be push to right")
        assertEquals(initialYVelocity, velocity.y, "The original vertical velocity must be preserved")
    }
}
