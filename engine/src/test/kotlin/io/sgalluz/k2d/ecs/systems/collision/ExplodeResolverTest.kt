package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.DeletionMark
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Velocity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ExplodeResolverTest {
    private val resolver = ExplodeResolver()
    private val manifold =
        CollisionManifold(
            overlapX = 1f,
            overlapY = 2f,
            deltaX = 0f,
            deltaY = 0f,
        )

    private fun entity(
        x: Float? = null,
        y: Float? = null,
        w: Float? = null,
        h: Float? = null,
        vx: Float? = null,
        vy: Float? = null,
        response: CollisionResponse = CollisionResponse.NONE,
    ): Entity =
        Entity(System.currentTimeMillis().toInt()).apply {
            if (x != null && y != null) add(Position(x, y))
            if (w != null && h != null) add(BoxCollider(w, h, response = response))
            if (vx != null && vy != null) add(Velocity(vx, vy))
        }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class ExplosionPush {
        @ParameterizedTest(name = "push on X axis, dx={0}")
        @MethodSource("xAxisCases")
        fun `bomb explodes and pushes victim on X axis`(
            dx: Float,
            expectedVelocityX: Float,
        ) {
            val bomb =
                entity(
                    x = 0f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    response = CollisionResponse.EXPLODE,
                )
            val victim =
                entity(
                    x = dx,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                )

            resolver.resolve(bomb, victim, manifold)

            val vel = victim.get<Velocity>()!!
            assertEquals(expectedVelocityX, vel.x)
            assertEquals(0f, vel.y)
            assertTrue(bomb.has<DeletionMark>())
            assertFalse(victim.has<DeletionMark>())
        }

        @ParameterizedTest(name = "push on Y axis, dy={0}")
        @MethodSource("yAxisCases")
        fun `bomb explodes and pushes victim on Y axis`(
            dy: Float,
            expectedVelocityY: Float,
        ) {
            val bomb =
                entity(
                    x = 0f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    response = CollisionResponse.EXPLODE,
                )
            val victim =
                entity(
                    x = 0f,
                    y = dy,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                )

            resolver.resolve(bomb, victim, manifold)

            val vel = victim.get<Velocity>()!!
            assertEquals(0f, vel.x)
            assertEquals(expectedVelocityY, vel.y)
        }

        fun xAxisCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(100f, 500f),
                Arguments.of(-100f, -500f),
            )

        fun yAxisCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(100f, 500f),
                Arguments.of(-100f, -500f),
            )
    }

    @Nested
    inner class ExplosionResolution {
        @Test
        fun `both entities explode and no push is applied`() {
            val e1 =
                entity(
                    x = 0f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                    response = CollisionResponse.EXPLODE,
                )
            val e2 =
                entity(
                    x = 100f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                    response = CollisionResponse.EXPLODE,
                )

            resolver.resolve(e1, e2, manifold)

            assertTrue(e1.has<DeletionMark>())
            assertTrue(e2.has<DeletionMark>())
            assertEquals(0f, e1.get<Velocity>()!!.x)
            assertEquals(0f, e2.get<Velocity>()!!.x)
        }

        @Test
        fun `no explosion response means no deletion and no push`() {
            val e1 =
                entity(
                    x = 0f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                )
            val e2 =
                entity(
                    x = 100f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                )

            resolver.resolve(e1, e2, manifold)

            assertFalse(e1.has<DeletionMark>())
            assertFalse(e2.has<DeletionMark>())
            assertEquals(0f, e1.get<Velocity>()!!.x)
            assertEquals(0f, e2.get<Velocity>()!!.x)
        }
    }

    @Nested
    inner class EarlyReturns {
        @Test
        fun `victim without velocity prevents push`() {
            val bomb =
                entity(
                    x = 0f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    response = CollisionResponse.EXPLODE,
                )
            val victim =
                entity(
                    x = 100f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                )

            resolver.resolve(bomb, victim, manifold)

            assertNull(victim.get<Velocity>())
        }

        @Test
        fun `victim without position prevents push`() {
            val bomb =
                entity(
                    x = 0f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    response = CollisionResponse.EXPLODE,
                )
            val victim =
                entity(
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                )

            resolver.resolve(bomb, victim, manifold)

            val vel = victim.get<Velocity>()!!
            assertEquals(0f, vel.x)
            assertEquals(0f, vel.y)
        }

        @Test
        fun `bomb without position prevents push`() {
            val bomb =
                entity(
                    w = 10f,
                    h = 10f,
                    response = CollisionResponse.EXPLODE,
                )
            val victim =
                entity(
                    x = 100f,
                    y = 0f,
                    w = 10f,
                    h = 10f,
                    vx = 0f,
                    vy = 0f,
                )

            resolver.resolve(bomb, victim, manifold)

            val vel = victim.get<Velocity>()!!
            assertEquals(0f, vel.x)
            assertEquals(0f, vel.y)
        }
    }
}
