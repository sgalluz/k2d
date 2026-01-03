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
            val pos = entity.get<Position>() ?: return@forEach
            val vel = entity.get<Velocity>() ?: return@forEach

            val (newX, newVelX) = bounce(pos.x, vel.x, 0f, width)
            val (newY, newVelY) = bounce(pos.y, vel.y, 0f, height)

            pos.x = newX
            pos.y = newY
            vel.x = newVelX
            vel.y = newVelY
        }
    }

    private fun bounce(
        position: Float,
        velocity: Float,
        min: Float,
        max: Float
    ): Pair<Float, Float> =
        when {
            position >= max && velocity > 0 -> max to -velocity
            position <= min && velocity < 0 -> min to -velocity
            else -> position to velocity
        }
}