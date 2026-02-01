package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.systems.GameSystem

class InputSystem(
    private val pressedKeys: List<Key>,
    private val speed: Float = 200f,
) : GameSystem {
    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val vel = entity.get<Velocity>() ?: return@forEach
            entity.get<PlayerInput>() ?: return@forEach

            val right = pressedKeys.contains(Key.DirectionRight)
            val left = pressedKeys.contains(Key.DirectionLeft)

            if (right && left) {
                vel.x = 0f
            } else if (right) {
                vel.x = speed
            } else if (left) {
                vel.x = -speed
            }

            val up = pressedKeys.contains(Key.DirectionUp)
            val down = pressedKeys.contains(Key.DirectionDown)

            if (up && down) {
                vel.y = 0f
            } else if (up) {
                vel.y = -speed
            } else if (down) {
                vel.y = speed
            }
        }
    }
}
