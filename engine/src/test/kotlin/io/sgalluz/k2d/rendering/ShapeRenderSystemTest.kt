package io.sgalluz.k2d.rendering

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Sprite
import io.mockk.mockk
import io.mockk.verify
import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.rendering.systems.ShapeRenderSystem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class ShapeRenderSystemTest {
    private lateinit var system: ShapeRenderSystem
    private lateinit var drawScope: DrawScope

    @BeforeEach
    fun setUp() {
        drawScope = mockk<DrawScope>(relaxed = true)
        system = ShapeRenderSystem()
    }

    @Test
    fun `render should draw only entities with both Position and Sprite`() {
        val entities = listOf(
            Entity(1)
                .add(Position(10f, 20f))
                .add(Sprite(Color.Blue, 50f)),
            Entity(2).add(Position(30f, 40f)),
            Entity(3)
        )

        system.render(entities, drawScope)

        verify(exactly = 1) {
            drawScope.drawRect(
                color = Color.Blue,
                topLeft = any(),
                size = any()
            )
        }
    }

    @Test
    fun `render draws entity in red when collider is colliding`() {
        val entities = listOf(
            Entity(1)
                .add(Position(10f, 20f))
                .add(Sprite(Color.Green, 32f))
                .add(
                    BoxCollider(
                        width = 32f,
                        height = 32f,
                        isColliding = true
                    )
                )
        )

        system.render(entities, drawScope)

        verify(exactly = 1) {
            drawScope.drawRect(
                color = Color.Red,
                topLeft = any(),
                size = any()
            )
        }
    }

    @Test
    fun `render maintains entity color when collider is not colliding`() {
        val entities = listOf(
            Entity(1)
                .add(Position(10f, 20f))
                .add(Sprite(Color.Green, 32f))
                .add(
                    BoxCollider(
                        width = 32f,
                        height = 32f,
                        isColliding = false
                    )
                )
        )

        system.render(entities, drawScope)

        verify(exactly = 1) {
            drawScope.drawRect(
                color = Color.Green,
                topLeft = any(),
                size = any()
            )
        }
    }
}