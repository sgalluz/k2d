package dev.sgalluz.k2d.ecs.systems.collision

import dev.sgalluz.k2d.ecs.BoxCollider
import dev.sgalluz.k2d.ecs.CollisionResponse
import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.Position
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class StaticResolverTest {
    private val resolver = StaticResolver()

    @Test
    fun `moves e1 on X axis when e1 is mobile and deltaX is negative`() {
        val e1 = entity(x = 0f, y = 0f, response = CollisionResponse.BOUNCE)
        val e2 = entity(x = 1f, y = 0f, response = CollisionResponse.STATIC)

        val manifold =
            CollisionManifold(
                overlapX = 1f,
                overlapY = 10f,
                deltaX = -1f,
                deltaY = 0f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!

        assertEquals(-1f, p1.x, 0.0001f)
        assertEquals(1f, p2.x, 0.0001f)
    }

    @Test
    fun `moves e1 on X axis when deltaX is positive`() {
        val e1 = entity(x = 1f, y = 0f, response = CollisionResponse.BOUNCE)
        val e2 = entity(x = 0f, y = 0f, response = CollisionResponse.STATIC)

        val manifold =
            CollisionManifold(
                overlapX = 1f,
                overlapY = 10f,
                deltaX = 1f,
                deltaY = 0f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!

        assertEquals(2f, p1.x, 0.0001f)
    }

    @Test
    fun `moves e2 on Y axis when e1 is static and deltaY is negative`() {
        val e1 = entity(x = 0f, y = 0f, response = CollisionResponse.STATIC)
        val e2 = entity(x = 0f, y = 1f, response = CollisionResponse.BOUNCE)

        val manifold =
            CollisionManifold(
                overlapX = 10f,
                overlapY = 1f,
                deltaX = 0f,
                deltaY = -1f,
            )

        resolver.resolve(e1, e2, manifold)

        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!

        assertEquals(2f, p2.y, 0.0002f)
        assertEquals(0f, p1.y, 0.0001f)
    }

    @Test
    fun `moves e2 on Y axis when deltaY is positive`() {
        val e1 = entity(x = 0f, y = 0f, response = CollisionResponse.STATIC)
        val e2 = entity(x = 0f, y = -1f, response = CollisionResponse.BOUNCE)

        val manifold =
            CollisionManifold(
                overlapX = 10f,
                overlapY = 1f,
                deltaX = 0f,
                deltaY = 1f,
            )

        resolver.resolve(e1, e2, manifold)

        val p2 = e2.get<Position>()!!

        assertEquals(-2f, p2.y, 0.0001f)
    }

    private fun entity(
        x: Float,
        y: Float,
        response: CollisionResponse,
    ): Entity =
        Entity(System.currentTimeMillis().toInt()).apply {
            add(Position(x, y))
            add(BoxCollider(width = 1f, height = 1f, response = response))
        }
}
