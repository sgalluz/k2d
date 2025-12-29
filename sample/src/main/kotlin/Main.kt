import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.sgalluz.k2d.rendering.K2DCanvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

fun main() = application {
    // State of our "player" (very rudimentary for now)
    var posX by remember { mutableStateOf(100f) }
    var posY by remember { mutableStateOf(100f) }
    var speedX = 200f // pixel al secondo
    var speedY = 150f

    Window(onCloseRequest = ::exitApplication, title = "K2D Engine - First Flight") {
        K2DCanvas(
            onUpdate = { delta ->
                // Time-based motion logic
                posX += speedX * delta
                posY += speedY * delta

                // Simple edge bounce (hardcoded for now)
                if (posX < 0f || posX > 600f) speedX *= -1
                if (posY < 0f || posY > 400f) speedY *= -1
            },
            onRender = {
                // Rendering of the square
                drawRect(
                    color = Color.Cyan,
                    topLeft = Offset(posX, posY),
                    size = Size(50f, 50f)
                )
            }
        )
    }
}