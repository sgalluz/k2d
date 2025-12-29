package io.sgalluz.k2d.rendering

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import io.sgalluz.k2d.core.rememberGameLoop

@Composable
fun K2DCanvas(
    onUpdate: (Float) -> Unit,
    onRender: DrawScope.() -> Unit
) {
    // We start the loop that calls onUpdate every frame
    val frameState = rememberGameLoop(onUpdate)

    // The Canvas redraws itself every time frameState.value changes
    Canvas(modifier = Modifier.fillMaxSize()) {
        // We use the value to force the state to be read and recomposed
        frameState.value
        onRender()
    }
}