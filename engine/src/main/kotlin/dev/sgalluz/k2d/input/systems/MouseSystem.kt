package dev.sgalluz.k2d.input.systems

import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.MouseFollower
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.systems.GameSystem
import dev.sgalluz.k2d.input.MouseState

class MouseSystem(private val mouseState: MouseState) : GameSystem {
    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            entity.get<MouseFollower>() ?: return@forEach
            val pos = entity.get<Position>() ?: return@forEach

            pos.x = mouseState.x
            pos.y = mouseState.y
        }
    }
}
