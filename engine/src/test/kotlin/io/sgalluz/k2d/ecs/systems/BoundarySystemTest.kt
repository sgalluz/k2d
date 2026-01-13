package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class BoundarySystemTest {
    private val boundarySystem = BoundarySystem(width = 800f, height = 600f)

    private fun entity(
        x: Float? = null,
        y: Float? = null,
        vx: Float? = null,
        vy: Float? = null,
        w: Float? = null,
        h: Float? = null
    ): Entity = Entity(1).apply {
        if (x != null && y != null) add(Position(x, y))
        if (vx != null && vy != null) add(Velocity(vx, vy))
        if (w != null && h != null) add(BoxCollider(w, h))
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class BoundsCorrection {

        @ParameterizedTest(name = "X axis correction: pos={0}, vel={1}")
        @MethodSource("xAxisCases")
        fun `entity is clamped and velocity adjusted on X axis`(
            startX: Float,
            startVelX: Float,
            expectedX: Float,
            expectedVelX: Float
        ) {
            val entity = entity(
                x = startX,
                y = 100f,
                vx = startVelX,
                vy = 0f
            )

            boundarySystem.update(listOf(entity), 0.016f)

            val pos = entity.get<Position>()!!
            val vel = entity.get<Velocity>()!!

            assertEquals(expectedX, pos.x)
            assertEquals(expectedVelX, vel.x)
        }

        @ParameterizedTest(name = "Y axis correction: pos={0}, vel={1}")
        @MethodSource("yAxisCases")
        fun `entity is clamped and velocity adjusted on Y axis`(
            startY: Float,
            startVelY: Float,
            expectedY: Float,
            expectedVelY: Float
        ) {
            val entity = entity(
                x = 100f,
                y = startY,
                vx = 0f,
                vy = startVelY
            )

            boundarySystem.update(listOf(entity), 0.016f)

            val pos = entity.get<Position>()!!
            val vel = entity.get<Velocity>()!!

            assertEquals(expectedY, pos.y)
            assertEquals(expectedVelY, vel.y)
        }

        fun xAxisCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(-10f, -20f, 0f, 20f),
                Arguments.of(-10f, 20f, 0f, 20f),
                Arguments.of(810f, 30f, 800f, -30f),
                Arguments.of(810f, -30f, 800f, -30f),
                Arguments.of(400f, 10f, 400f, 10f)
            )

        fun yAxisCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(-10f, -20f, 0f, 20f),
                Arguments.of(-10f, 20f, 0f, 20f),
                Arguments.of(610f, 30f, 600f, -30f),
                Arguments.of(610f, -30f, 600f, -30f),
                Arguments.of(300f, 10f, 300f, 10f)
            )
    }

    @Nested
    inner class ColliderAwareBounds {

        @Test
        fun `entity respects width when clamped on X axis`() {
            val entity = entity(
                x = 790f,
                y = 100f,
                vx = 100f,
                vy = 0f,
                w = 50f,
                h = 50f
            )

            boundarySystem.update(listOf(entity), 0.016f)

            val pos = entity.get<Position>()!!
            assertEquals(750f, pos.x) // 800 - 50
        }

        @Test
        fun `entity respects height when clamped on Y axis`() {
            val entity = entity(
                x = 100f,
                y = 590f,
                vx = 0f,
                vy = 100f,
                w = 50f,
                h = 50f
            )

            boundarySystem.update(listOf(entity), 0.016f)

            val pos = entity.get<Position>()!!
            assertEquals(550f, pos.y) // 600 - 50
        }
    }

    @Nested
    inner class MissingComponents {

        @Test
        fun `entity without Position is ignored`() {
            val entity = Entity(1)
                .add(Velocity(10f, 10f))

            assertDoesNotThrow {
                boundarySystem.update(listOf(entity), 0.016f)
            }
        }

        @Test
        fun `entity without Velocity is clamped but velocity is not modified`() {
            val entity = entity(
                x = -10f,
                y = 100f
            )

            boundarySystem.update(listOf(entity), 0.016f)

            val pos = entity.get<Position>()!!
            assertEquals(0f, pos.x)
            assertNull(entity.get<Velocity>())
        }

        @Test
        fun `entity without BoxCollider uses zero size`() {
            val entity = entity(
                x = 810f,
                y = 100f,
                vx = 50f,
                vy = 0f
            )

            boundarySystem.update(listOf(entity), 0.016f)

            val pos = entity.get<Position>()!!
            val vel = entity.get<Velocity>()!!

            assertEquals(800f, pos.x)
            assertEquals(-50f, vel.x)
        }
    }
}