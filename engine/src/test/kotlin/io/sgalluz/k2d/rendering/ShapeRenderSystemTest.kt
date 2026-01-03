package io.sgalluz.k2d.rendering

import androidx.compose.ui.graphics.Color
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Sprite
import io.mockk.mockk
import io.mockk.verify
import io.sgalluz.k2d.rendering.systems.ShapeRenderSystem
import org.junit.jupiter.api.Test

class ShapeRenderSystemTest {

    @Test
    fun `render should draw only entities with both Position and Sprite`() {
        // Arrange
        val drawScope = mockk<androidx.compose.ui.graphics.drawscope.DrawScope>(relaxed = true)
        val system = ShapeRenderSystem()

        val validEntity = Entity(1)
            .add(Position(10f, 20f))
            .add(Sprite(Color.Red, 50f))

        val invalidEntity = Entity(2)
            .add(Position(30f, 40f))

        val entities = listOf(validEntity, invalidEntity)

        // Act
        system.render(entities, drawScope)

        // Assert
        verify(exactly = 1) {
            drawScope.drawRect(
                color = Color.Red,
                topLeft = any(),
                size = any()
            )
        }
    }
}