import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.sgalluz.k2d.ecs.*
import io.sgalluz.k2d.ecs.systems.BoundarySystem
import io.sgalluz.k2d.ecs.systems.CollisionSystem
import io.sgalluz.k2d.ecs.systems.MovementSystem
import io.sgalluz.k2d.input.InputSystem
import io.sgalluz.k2d.rendering.k2dCanvas
import io.sgalluz.k2d.rendering.systems.ShapeRenderSystem

fun main() = application {
    // 1. INPUT STATE
    // We use a Compose reactive list to track currently pressed keys.
    // This allows the InputSystem to see changes in real-time.
    val pressedKeys = remember { mutableStateListOf<Key>() }

    // 2. THE ECS WORLD (Pure logic and data)
    val world = remember {
        World().apply {
            // Internal logic systems
            addSystem(MovementSystem())
            addSystem(BoundarySystem(width = 800f, height = 600f))
            addSystem(CollisionSystem())

            // Create "Player" entity (Cyan)
            // Starts idle (0,0). Velocity is controlled entirely by InputSystem.
            createEntity()
                .add(Position(400f, 300f))
                .add(Velocity(0f, 0f))
                .add(Sprite(Color.Cyan, 50f))
                .add(BoxCollider(width = 50f, height = 50f))
                .add(PlayerInput())

            // Create "NPC" entity (Magenta)
            // Moves autonomously and bounces off boundaries.
            createEntity()
                .add(Position(100f, 100f))
                .add(Velocity(150f, 150f))
                .add(Sprite(Color.Magenta, 30f))
                .add(BoxCollider(width = 30f, height = 30f))

            // WALL ENTITY (Dark Gray)
            // A static obstacle that cannot be moved.
            createEntity()
                .add(Position(250f, 200f))
                .add(Sprite(Color.DarkGray, 100f))
                .add(BoxCollider(width = 100f, height = 100f, isStatic = true))
        }
    }

    // 3. EXTERNAL SYSTEMS (Adapters for Input and Rendering)
    // We pass the mutable list instance so the system can observe key changes.
    val inputSystem = remember { InputSystem(pressedKeys) }
    val renderer = remember { ShapeRenderSystem() }

    Window(
        onCloseRequest = ::exitApplication,
        title = "K2D Engine - Player vs NPC",
        // 4. KEYBOARD EVENT CAPTURE
        onKeyEvent = { keyEvent ->
            when (keyEvent.type) {
                KeyEventType.KeyDown -> {
                    if (!pressedKeys.contains(keyEvent.key)) pressedKeys.add(keyEvent.key)
                }
                KeyEventType.KeyUp -> {
                    pressedKeys.remove(keyEvent.key)
                }
            }
            true // Mark event as handled
        }
    ) {
        k2dCanvas(
            onUpdate = { deltaTime ->
                // 5. ENGINE HEARTBEAT
                // First, update player velocity based on input
                inputSystem.update(world.getEntities(), deltaTime)

                // Then, run the physics simulation and constraints
                // world.update now runs: Movement -> Boundary -> Collision
                world.update(deltaTime)
            },
            onRender = {
                // 6. RENDERING
                renderer.render(world.getEntities(), this)
            }
        )
    }
}