package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Velocity

class MovementSystem : GameSystem {
    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val pos = entity.get<Position>()
            val vel = entity.get<Velocity>()

            if (pos != null && vel != null) {
                pos.x += vel.x * deltaTime
                pos.y += vel.y * deltaTime
            }
        }
    }
}
