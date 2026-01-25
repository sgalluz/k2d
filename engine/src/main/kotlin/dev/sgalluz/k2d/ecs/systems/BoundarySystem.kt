package dev.sgalluz.k2d.ecs.systems

import dev.sgalluz.k2d.ecs.BoxCollider
import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity

class BoundarySystem(
    private val width: Float,
    private val height: Float,
) : GameSystem {
    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val pos = entity.get<Position>() ?: return@forEach
            val col = entity.get<BoxCollider>()
            val vel = entity.get<Velocity>()

            val (newX, newVelX) =
                applyBounds(
                    pos.x,
                    vel?.x ?: 0f,
                    col?.width ?: 0f,
                    width,
                )
            pos.x = newX
            vel?.let { it.x = newVelX }

            val (newY, newVelY) =
                applyBounds(
                    pos.y,
                    vel?.y ?: 0f,
                    col?.height ?: 0f,
                    height,
                )
            pos.y = newY
            vel?.let { it.y = newVelY }
        }
    }

    private fun applyBounds(
        position: Float,
        velocity: Float,
        size: Float,
        maxLimit: Float,
    ): Pair<Float, Float> {
        val minLimit = 0f
        val actualMax = maxLimit - size

        return when {
            position < minLimit -> minLimit to if (velocity < 0) -velocity else velocity
            position > actualMax -> actualMax to if (velocity > 0) -velocity else velocity
            else -> position to velocity
        }
    }
}
