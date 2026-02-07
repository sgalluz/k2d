package dev.sgalluz.k2d.ecs.systems

import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.Friction
import dev.sgalluz.k2d.ecs.Velocity
import kotlin.math.abs

class FrictionSystem(private val globalFriction: Float = 0.1f) : GameSystem {
    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val vel = entity.get<Velocity>() ?: return@forEach
            val frictionValue = entity.get<Friction>()?.linearDrag ?: globalFriction
            vel.x = applyFriction(vel.x, frictionValue, deltaTime)
            vel.y = applyFriction(vel.y, frictionValue, deltaTime)
        }
    }

    private fun applyFriction(
        currentVel: Float,
        friction: Float,
        dt: Float,
    ): Float {
        val reduction = 1f - (friction * dt)
        val newVel = currentVel * reduction
        return if (abs(newVel) < 0.1f) 0f else newVel
    }
}
