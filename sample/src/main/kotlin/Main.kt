import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.isPrimaryPressed
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.sgalluz.k2d.ecs.BoxCollider
import dev.sgalluz.k2d.ecs.CollisionResponse
import dev.sgalluz.k2d.ecs.Friction
import dev.sgalluz.k2d.ecs.MouseFollower
import dev.sgalluz.k2d.ecs.PlayerInput
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Sprite
import dev.sgalluz.k2d.ecs.Velocity
import dev.sgalluz.k2d.ecs.World
import dev.sgalluz.k2d.ecs.systems.BoundarySystem
import dev.sgalluz.k2d.ecs.systems.CollisionSystem
import dev.sgalluz.k2d.ecs.systems.FrictionSystem
import dev.sgalluz.k2d.ecs.systems.MovementSystem
import dev.sgalluz.k2d.input.InputSystem
import dev.sgalluz.k2d.input.MouseState
import dev.sgalluz.k2d.input.MouseSystem
import dev.sgalluz.k2d.rendering.k2dCanvas
import dev.sgalluz.k2d.rendering.systems.ShapeRenderSystem

fun main() =
    application {
        // 1. SHARED INPUT STATE
        val pressedKeys = remember { mutableStateListOf<Key>() }
        val mouseState = remember { MouseState() }

        // 2. EXTERNAL SYSTEMS INITIALIZATION
        val inputSystem = remember { InputSystem(pressedKeys) }
        val mouseSystem = remember { MouseSystem(mouseState) }
        val renderer = remember { ShapeRenderSystem() }

        // 3. THE ECS WORLD SETUP
        val world =
            remember {
                World().apply {
                    // Core Logic Systems
                    addSystem(MovementSystem())
                    addSystem(FrictionSystem(globalFriction = 10.0f))
                    addSystem(CollisionSystem())
                    addSystem(BoundarySystem(width = 800f, height = 600f))

                    // Player Entity (Cyan)
                    createEntity()
                        .add(Position(400f, 300f))
                        .add(Velocity(0f, 0f))
                        .add(Sprite(Color.Cyan, 50f))
                        .add(BoxCollider(width = 50f, height = 50f, response = CollisionResponse.BOUNCE))
                        .add(PlayerInput())

                    // Crosshair Entity (White - Follows Mouse)
                    createEntity()
                        .add(Position(0f, 0f))
                        .add(Sprite(Color.White, 10f))
                        .add(MouseFollower())

                    // Autonomous NPC (Magenta)
                    createEntity()
                        .add(Position(100f, 100f))
                        .add(Velocity(150f, 100f))
                        .add(Sprite(Color.Magenta, 30f))
                        .add(BoxCollider(width = 30f, height = 30f, response = CollisionResponse.BOUNCE))
                        .add(Friction(linearDrag = 0f))

                    // Static Obstacle (Dark Gray)
                    createEntity()
                        .add(Position(500f, 100f))
                        .add(Sprite(Color.DarkGray, 100f))
                        .add(BoxCollider(width = 100f, height = 100f, response = CollisionResponse.STATIC))

                    // Pushable Box (Yellow)
                    createEntity()
                        .add(Position(200f, 400f))
                        .add(Velocity(0f, 0f))
                        .add(Sprite(Color.Yellow, 40f))
                        .add(BoxCollider(width = 40f, height = 40f, response = CollisionResponse.PUSH))
                        .add(Friction(linearDrag = 0f))

                    // Explosive Mines (Red)
                    val mineCoords = listOf(300f to 200f, 250f to 410f, 600f to 450f, 615f to 450f)
                    mineCoords.forEach { (x, y) ->
                        createEntity()
                            .add(Position(x, y))
                            .add(Sprite(Color.Red, 20f))
                            .add(BoxCollider(width = 20f, height = 20f, response = CollisionResponse.EXPLODE))
                    }
                }
            }

        Window(
            onCloseRequest = ::exitApplication,
            title = "K2D Engine - Mouse & Configurable Input",
            onKeyEvent = { keyEvent ->
                when (keyEvent.type) {
                    KeyEventType.KeyDown -> if (!pressedKeys.contains(keyEvent.key)) pressedKeys.add(keyEvent.key)
                    KeyEventType.KeyUp -> pressedKeys.remove(keyEvent.key)
                }
                true
            },
        ) {
            k2dCanvas(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    val position = event.changes.first().position

                                    // Update MouseState with latest hardware events
                                    mouseState.x = position.x
                                    mouseState.y = position.y
                                    mouseState.isLeftPressed = event.buttons.isPrimaryPressed
                                    mouseState.isRightPressed = event.buttons.isSecondaryPressed
                                }
                            }
                        },
                onUpdate = { deltaTime ->
                    // Process Input Systems
                    inputSystem.update(world.getEntities(), deltaTime)
                    mouseSystem.update(world.getEntities(), deltaTime)

                    // Process Physics and World Simulation
                    world.update(deltaTime)
                },
                onRender = { renderer.render(world.getEntities(), this) },
            )
        }
    }
