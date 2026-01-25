package dev.sgalluz.k2d.rendering

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.sgalluz.k2d.runtime.compose.rememberGameLoop

@Composable
fun k2dCanvas(
    onUpdate: (Float) -> Unit,
    onRender: DrawScope.() -> Unit,
) {
    val gameLoop = rememberGameLoop(onUpdate)
    Canvas(modifier = Modifier.fillMaxSize()) {
        gameLoop.frameTick.value
        onRender()
    }
}
