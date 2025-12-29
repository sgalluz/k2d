package io.sgalluz.k2d.core

import androidx.compose.runtime.*
import kotlinx.coroutines.isActive

@Composable
fun rememberGameLoop(onUpdate: (Float) -> Unit): State<Long> {
    // This state triggers the recomposition of the Compose Canvas
    val frameClock = remember { mutableStateOf(0L) }

    LaunchedEffect(Unit) {
        var lastFrameTime = 0L

        while (isActive) {
            withFrameNanos { frameTimeNanos ->
                if (lastFrameTime != 0L) {
                    // Calculate delta time in seconds
                    val deltaTime = (frameTimeNanos - lastFrameTime) / 1_000_000_000f

                    // Execute game logic (physics, movement, etc.)
                    onUpdate(deltaTime)
                }
                lastFrameTime = frameTimeNanos
                frameClock.value = frameTimeNanos
            }
        }
    }
    return frameClock
}