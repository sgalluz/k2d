package io.sgalluz.k2d.input

import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.PlayerInput
import io.sgalluz.k2d.ecs.Velocity
import io.sgalluz.k2d.ecs.systems.GameSystem

class InputSystem(private val pressedKeys: Set<String>) : GameSystem {
    private val speed = 200f

    override fun update(entities: List<Entity>, deltaTime: Float) {
        entities.forEach { entity ->
            val input = entity.get<PlayerInput>()
            val vel = entity.get<Velocity>()

            if (input != null && vel != null) {
                var newX = 0f
                var newY = 0f

                if (pressedKeys.contains("ArrowRight")) newX = speed
                if (pressedKeys.contains("ArrowLeft")) newX = -speed
                if (pressedKeys.contains("ArrowDown")) newY = speed
                if (pressedKeys.contains("ArrowUp")) newY = -speed

                vel.x = newX
                vel.y = newY
            }
        }
    }
}