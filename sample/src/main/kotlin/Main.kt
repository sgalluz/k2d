import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.sgalluz.k2d.ecs.*
import io.sgalluz.k2d.ecs.systems.MovementSystem
import io.sgalluz.k2d.rendering.k2dCanvas
import io.sgalluz.k2d.ecs.systems.BoundarySystem
import io.sgalluz.k2d.rendering.systems.ShapeRenderSystem

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

    // 3. Initialize the rendering systems (outside the World)
    val renderer = remember { ShapeRenderSystem() }

    Window(onCloseRequest = ::exitApplication, title = "K2D Engine - ECS in Action") {
        k2dCanvas(
            onUpdate = { deltaTime ->
                // 4. The engine heartbeat now drives the World
                world.update(deltaTime)
            },
            onRender = {
                // 5. Let's pass the entities to the renderer manually
                renderer.render(world.getEntities(), this)
            }
        )
    }
}