package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.systems.GameSystem

class InputSystem(
    pressedKeys: Collection<Key>,
    private val speed: Float = 200f,
    private val config: InputConfig = InputConfig(),
) : GameSystem {
    private val keys = pressedKeys.toSet()

    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val velocity = entity.get<Velocity>() ?: return@forEach
            if (entity.get<PlayerInput>() == null) return@forEach

            val horizontal =
                direction(
                    negative = isPressed(InputAction.LEFT),
                    positive = isPressed(InputAction.RIGHT),
                )

            val vertical =
                direction(
                    negative = isPressed(InputAction.UP),
                    positive = isPressed(InputAction.DOWN),
                )

            if (horizontal != 0f) velocity.x = horizontal * speed

            if (vertical != 0f) velocity.y = vertical * speed
        }
    }

    private fun isPressed(action: InputAction): Boolean = keys.contains(config.bindings[action])

    private fun direction(
        negative: Boolean,
        positive: Boolean,
    ): Float =
        when {
            negative && positive -> 0f
            negative -> -1f
            positive -> 1f
            else -> 0f
        }
}
