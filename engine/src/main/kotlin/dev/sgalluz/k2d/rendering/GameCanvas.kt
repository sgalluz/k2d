package dev.sgalluz.k2d.rendering

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import dev.sgalluz.k2d.runtime.compose.LocalGameLoopClock

@Composable
fun k2dCanvas(
    onRender: DrawScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    val clock = LocalGameLoopClock.current

    Canvas(modifier = modifier.fillMaxSize()) {
        clock.frameTick.value
        onRender()
    }
}
