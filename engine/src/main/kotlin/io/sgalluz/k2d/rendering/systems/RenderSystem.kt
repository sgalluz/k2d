package io.sgalluz.k2d.rendering.systems

import androidx.compose.ui.graphics.drawscope.DrawScope
import io.sgalluz.k2d.ecs.Entity

/**
 * Interface for systems that need to draw on screen.
 */
interface RenderSystem {
    fun render(entities: List<Entity>, drawScope: DrawScope)
}