import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.sgalluz.k2d.ecs.*
import io.sgalluz.k2d.ecs.systems.MovementSystem
import io.sgalluz.k2d.rendering.k2dCanvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import io.sgalluz.k2d.ecs.systems.BoundarySystem

fun main() = application {
    // 1. Initialize the World and Systems
    val world = remember {
        World().apply {
            addSystem(MovementSystem())
            addSystem(BoundarySystem(width = 800f, height = 600f))

            // 2. Create our test entity using ECS
            createEntity()
                .add(Position(100f, 100f))
                .add(Velocity(150f, 120f))
                .add(Sprite(Color.Cyan, 50f))
        }
    }

    Window(onCloseRequest = ::exitApplication, title = "K2D Engine - ECS in Action") {
        k2dCanvas(
            onUpdate = { deltaTime ->
                // 3. The engine heartbeat now drives the World
                world.update(deltaTime)
            },
            onRender = {
                // 4. For now, we render manually, but soon we'll have a RenderSystem
                world.getEntities().forEach { entity ->
                    val pos = entity.get<Position>()
                    val sprite = entity.get<Sprite>()

                    if (pos != null && sprite != null) {
                        drawRect(
                            color = sprite.color,
                            topLeft = Offset(pos.x, pos.y),
                            size = Size(sprite.size, sprite.size)
                        )
                    }
                }
            }
        )
    }
}