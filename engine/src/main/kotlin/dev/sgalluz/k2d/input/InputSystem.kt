package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.systems.GameSystem

class InputSystem(
    private val pressedKeys: List<Key>,
    private val speed: Float = 200f,
    private val config: InputConfig = InputConfig(),
) : GameSystem {
    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val vel = entity.get<Velocity>() ?: return@forEach
            entity.get<PlayerInput>() ?: return@forEach

            val up = pressedKeys.contains(config.bindings[InputAction.UP])
            val down = pressedKeys.contains(config.bindings[InputAction.DOWN])
            val left = pressedKeys.contains(config.bindings[InputAction.LEFT])
            val right = pressedKeys.contains(config.bindings[InputAction.RIGHT])

            if (left && right) {
                vel.x = 0f
            } else if (left) {
                vel.x = -speed
            } else if (right) {
                vel.x = speed
            }

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
