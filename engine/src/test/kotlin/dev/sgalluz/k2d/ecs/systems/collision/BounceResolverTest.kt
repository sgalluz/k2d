package dev.sgalluz.k2d.ecs.systems.collision

import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BounceResolverTest {
    private val resolver = BounceResolver()

    @Test
    fun `resolves collision along X axis`() {
        val e1 = entity(x = 0f, y = 0f, vx = 1f, vy = 0f)
        val e2 = entity(x = 1f, y = 0f, vx = -1f, vy = 0f)

        val manifold =
            CollisionManifold(
                overlapX = 1f,
                overlapY = 3f,
                deltaX = -1f,
                deltaY = 0f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!
        val v1 = e1.get<Velocity>()!!
        val v2 = e2.get<Velocity>()!!

        assertEquals(-0.5f, p1.x, 0.0001f)
        assertEquals(1.5f, p2.x, 0.0001f)

        assertEquals(-1f, v1.x)
        assertEquals(1f, v2.x)

        assertEquals(0f, p1.y)
        assertEquals(0f, p2.y)
    }

    @Test
    fun `resolves collision along Y axis`() {
        val e1 = entity(x = 0f, y = 0f, vx = 0f, vy = 1f)
        val e2 = entity(x = 0f, y = 1f, vx = 0f, vy = -1f)

        val manifold =
            CollisionManifold(
                overlapX = 3f,
                overlapY = 1f,
                deltaX = 0f,
                deltaY = -1f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!
        val v1 = e1.get<Velocity>()!!
        val v2 = e2.get<Velocity>()!!

        assertEquals(-0.5f, p1.y, 0.0001f)
        assertEquals(1.5f, p2.y, 0.0001f)

        assertEquals(-1f, v1.y)
        assertEquals(1f, v2.y)

        assertEquals(0f, p1.x)
        assertEquals(0f, p2.x)
    }

    @Test
    fun `collision pushes entities apart symmetrically on X axis`() {
        val e1 = entity(x = 0f, y = 0f, vx = 0f, vy = 0f)
        val e2 = entity(x = 0.8f, y = 0f, vx = 0f, vy = 0f)

        val manifold =
            CollisionManifold(
                overlapX = 0.4f,
                overlapY = 10f,
                deltaX = -0.8f,
                deltaY = 0f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!

        val distance = p2.x - p1.x
        assertEquals(1.2f, distance, 0.0001f)
    }

    @Test
    fun `resolves collision along X axis when deltaX is positive`() {
        val e1 = entity(x = 1f, y = 0f, vx = 1f, vy = 0f)
        val e2 = entity(x = 0f, y = 0f, vx = -1f, vy = 0f)

        val manifold =
            CollisionManifold(
                overlapX = 1f,
                overlapY = 3f,
                deltaX = 1f,
                deltaY = 0f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!
        val v1 = e1.get<Velocity>()!!
        val v2 = e2.get<Velocity>()!!

        assertEquals(1.5f, p1.x, 0.0001f)
        assertEquals(-0.5f, p2.x, 0.0001f)

        assertEquals(-1f, v1.x)
        assertEquals(1f, v2.x)
    }

    @Test
    fun `resolves collision along Y axis when deltaY is positive`() {
        val e1 = entity(x = 0f, y = 1f, vx = 0f, vy = 1f)
        val e2 = entity(x = 0f, y = 0f, vx = 0f, vy = -1f)

        val manifold =
            CollisionManifold(
                overlapX = 3f,
                overlapY = 1f,
                deltaX = 0f,
                deltaY = 1f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!
        val v1 = e1.get<Velocity>()!!
        val v2 = e2.get<Velocity>()!!

        assertEquals(1.5f, p1.y, 0.0001f)
        assertEquals(-0.5f, p2.y, 0.0001f)

        assertEquals(-1f, v1.y)
        assertEquals(1f, v2.y)
    }

    private fun entity(
        x: Float,
        y: Float,
        vx: Float,
        vy: Float,
    ): Entity =
        Entity(System.currentTimeMillis().toInt()).apply {
            add(Position(x, y))
            add(Velocity(vx, vy))
        }
}
