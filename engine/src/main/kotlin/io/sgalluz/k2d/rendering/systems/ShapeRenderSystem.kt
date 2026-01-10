package io.sgalluz.k2d.rendering.systems

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Sprite

class ShapeRenderSystem : RenderSystem {
    override fun render(entities: List<Entity>, drawScope: DrawScope) {
        entities.forEach { entity ->
            val pos = entity.get<Position>()
            val sprite = entity.get<Sprite>()
            val collider = entity.get<BoxCollider>()

            if (pos != null && sprite != null) {
                val drawColor: Color = if (collider?.isColliding == true) Color.Red else sprite.color
                drawScope.drawRect(
                    color = drawColor,
                    topLeft = Offset(pos.x, pos.y),
                    size = Size(sprite.size, sprite.size)
                )
            }
        }
    }
}