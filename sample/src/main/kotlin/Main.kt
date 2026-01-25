import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.sgalluz.k2d.ecs.BoxCollider
import dev.sgalluz.k2d.ecs.CollisionResponse
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Sprite
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import dev.sgalluz.k2d.ecs.systems.BoundarySystem
import dev.sgalluz.k2d.ecs.systems.CollisionSystem
import dev.sgalluz.k2d.ecs.systems.MovementSystem
import dev.sgalluz.k2d.input.InputSystem
import dev.sgalluz.k2d.rendering.k2dCanvas
import dev.sgalluz.k2d.rendering.systems.ShapeRenderSystem

fun main() =
    application {
        // 1. INPUT STATE
        // We use a Compose reactive list to track currently pressed keys.
        // This allows the InputSystem to see changes in real-time.
        val pressedKeys = remember { mutableStateListOf<Key>() }

        // 2. THE ECS WORLD (Pure logic and data)
        val world =
            remember {
                World().apply {
                    // Internal logic systems
                    addSystem(MovementSystem())
                    addSystem(CollisionSystem())
                    addSystem(BoundarySystem(width = 800f, height = 600f))

                    // Create "Player" entity (Cyan)
                    // It will bounce off the walls and the NPC, but will move the crate (thanks to the Dispatcher)
                    createEntity()
                        .add(Position(400f, 300f))
                        .add(Velocity(0f, 0f))
                        .add(Sprite(Color.Cyan, 50f))
                        .add(BoxCollider(width = 50f, height = 50f, response = CollisionResponse.BOUNCE))
                        .add(PlayerInput())

                    // Create "NPC" entity (Magenta)
                    // Moves autonomously and bounces off boundaries.
                    createEntity()
                        .add(Position(100f, 100f))
                        .add(Velocity(150f, 100f))
                        .add(Sprite(Color.Magenta, 30f))
                        .add(BoxCollider(width = 30f, height = 30f, response = CollisionResponse.BOUNCE))

                    // WALL ENTITY (Dark Gray)
                    // A static obstacle that cannot be moved.
                    createEntity()
                        .add(Position(500f, 100f))
                        .add(Sprite(Color.DarkGray, 100f))
                        .add(BoxCollider(width = 100f, height = 100f, response = CollisionResponse.STATIC))

                    // BOX (Yellow)
                    // Demonstrates the PUSH: if the player walks against it, it will move it without bouncing back.
                    createEntity()
                        .add(Position(200f, 400f))
                        .add(Velocity(0f, 0f))
                        .add(Sprite(Color.Yellow, 40f))
                        .add(BoxCollider(width = 40f, height = 40f, response = CollisionResponse.PUSH))

                    // MINE 1 (Red)
                    createEntity()
                        .add(Position(300f, 200f))
                        .add(Sprite(Color.Red, 20f))
                        .add(BoxCollider(width = 20f, height = 20f, response = CollisionResponse.EXPLODE))

                    // MINE 2 (RED) - Near the box to test the reaction
                    createEntity()
                        .add(Position(250f, 410f))
                        .add(Sprite(Color.Red, 20f))
                        .add(BoxCollider(width = 20f, height = 20f, response = CollisionResponse.EXPLODE))

                    // PAIR OF CLOSE MINES - To test the chain reaction
                    createEntity()
                        .add(Position(600f, 450f))
                        .add(Sprite(Color.Red, 20f))
                        .add(BoxCollider(width = 20f, height = 20f, response = CollisionResponse.EXPLODE))
                    createEntity()
                        .add(Position(615f, 450f))
                        .add(Sprite(Color.Red, 20f))
                        .add(BoxCollider(width = 20f, height = 20f, response = CollisionResponse.EXPLODE))
                }
            }

        // 3. EXTERNAL SYSTEMS (Adapters for Input and Rendering)
        // We pass the mutable list instance so the system can observe key changes.
        val inputSystem = remember { InputSystem(pressedKeys) }
        val renderer = remember { ShapeRenderSystem() }

        Window(
            onCloseRequest = ::exitApplication,
            title = "K2D Engine - Collision Strategies",
            // 4. KEYBOARD EVENT CAPTURE
            onKeyEvent = { keyEvent ->
                when (keyEvent.type) {
                    KeyEventType.KeyDown -> if (!pressedKeys.contains(keyEvent.key)) pressedKeys.add(keyEvent.key)
                    KeyEventType.KeyUp -> pressedKeys.remove(keyEvent.key)
                }
                true // Mark event as handled
            },
        ) {
            k2dCanvas(
                // 5. ENGINE HEARTBEAT
                onUpdate = { deltaTime ->
                    // First, update player velocity based on input
                    inputSystem.update(world.getEntities(), deltaTime)

                    // Then, run the physics simulation and constraints
                    // world.update now runs: Movement -> Boundary -> Collision
                    world.update(deltaTime)
                },
                // 6. RENDERING
                onRender = { renderer.render(world.getEntities(), this) },
            )
        }
    }
