package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.GameSystem
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Velocity

class BoundarySystem(
    private val width: Float,
    private val height: Float
) : GameSystem {
    override fun update(entities: List<Entity>, deltaTime: Float) {
        entities.forEach { entity ->
            val pos = entity.get<Position>()
            val vel = entity.get<Velocity>()

            if (pos != null && vel != null) {
                // Check Right and Left
                if (pos.x >= width && vel.x > 0) {
                    vel.x *= -1f
                    pos.x = width // Snap back to edge
                } else if (pos.x <= 0f && vel.x < 0) {
                    vel.x *= -1f
                    pos.x = 0f // Snap back to edge
                }

                // Check Bottom and Top
                if (pos.y >= height && vel.y > 0) {
                    vel.y *= -1f
                    pos.y = height
                } else if (pos.y <= 0f && vel.y < 0) {
                    vel.y *= -1f
                    pos.y = 0f
                }
            }
        }
    }
}