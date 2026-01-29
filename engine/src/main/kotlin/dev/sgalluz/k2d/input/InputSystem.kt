package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key
import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.systems.GameSystem

class InputSystem(private val pressedKeys: List<Key>) : GameSystem {
    private val speed = 200f

    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { entity ->
            val vel = entity.get<Velocity>() ?: return@forEach
            if (entity.get<PlayerInput>() == null) return@forEach

            var deltaX = 0f
            var deltaY = 0f

            if (pressedKeys.contains(Key.DirectionRight)) deltaX += 1f
            if (pressedKeys.contains(Key.DirectionLeft)) deltaX -= 1f
            if (pressedKeys.contains(Key.DirectionDown)) deltaY += 1f
            if (pressedKeys.contains(Key.DirectionUp)) deltaY -= 1f

            vel.x = deltaX * speed
            vel.y = deltaY * speed

            /* FIXME: If we want smooth acceleration, we would use deltaTime here
             *  but we should also define the concept of acceleration:
             *  vel.x += deltaX * acceleration * deltaTime
             */
        }
    }
}
