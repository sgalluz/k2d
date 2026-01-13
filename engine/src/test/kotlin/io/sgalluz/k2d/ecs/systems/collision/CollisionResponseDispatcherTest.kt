package io.sgalluz.k2d.ecs.systems.collision

import io.mockk.*
import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.CollisionResponse.*
import io.sgalluz.k2d.ecs.Entity
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class CollisionResponseDispatcherTest {
    private val dispatcher = CollisionResponseDispatcher()

    private val manifold = CollisionManifold(
        overlapX = 1f,
        overlapY = 2f,
        deltaX = 0f,
        deltaY = 0f
    )

    private lateinit var resolver: CollisionResolver

    @BeforeEach
    fun setUp() {
        mockkObject(CollisionResolverFactory)
        resolver = mockk(relaxed = true)
    }

    @AfterEach
    fun tearDown() = unmockkAll()

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class DispatchBehaviour {
        @ParameterizedTest(
            name = "r1={0}, r2={1} -> expected resolver={2}"
        )
        @MethodSource("dispatchCases")
        fun `dispatcher selects correct resolver and invokes it`(
            r1: CollisionResponse,
            r2: CollisionResponse,
            expected: CollisionResponse
        ) {
            every {
                CollisionResolverFactory.getResolver(expected)
            } returns resolver

            val e1 = entityWithResponse(r1)
            val e2 = entityWithResponse(r2)

            assertDoesNotThrow {
                dispatcher.dispatch(e1, e2, manifold)
            }

            verify(exactly = 1) {
                resolver.resolve(e1, e2, manifold)
            }

            confirmVerified(resolver)
        }

        fun dispatchCases(): Stream<Arguments> =
            Stream.of(
                // EXPLODE
                Arguments.of(EXPLODE, BOUNCE, EXPLODE),
                Arguments.of(PUSH, EXPLODE, EXPLODE),

                // STATIC
                Arguments.of(STATIC, BOUNCE, STATIC),
                Arguments.of(PUSH, STATIC, STATIC),

                // BOUNCE
                Arguments.of(BOUNCE, BOUNCE, BOUNCE),

                // PUSH
                Arguments.of(PUSH, PUSH, PUSH),
                Arguments.of(PUSH, NONE, PUSH)
            )
    }

    private fun entityWithResponse(response: CollisionResponse): Entity =
        Entity(1).apply {
            add(BoxCollider(10f, 10f, response = response))
        }
}
