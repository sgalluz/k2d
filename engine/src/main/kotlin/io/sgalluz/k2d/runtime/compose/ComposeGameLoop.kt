package io.sgalluz.k2d.runtime.compose

import androidx.compose.runtime.*
import kotlinx.coroutines.isActive
import io.sgalluz.k2d.core.GameLoop
import io.sgalluz.k2d.runtime.GameLoopClock

@Composable
fun rememberGameLoop(
    onUpdate: (Float) -> Unit
): GameLoopClock {
    val gameLoop = remember { GameLoop(onUpdate = onUpdate) }
    val frameState = remember { mutableStateOf(0L) }

    LaunchedEffect(gameLoop) {
        while (isActive) {
            withFrameNanos { frameNanos ->
                gameLoop.update(frameNanos)
                frameState.value = frameNanos
            }
        }
    }

    return remember {
        object : GameLoopClock {
            override val frameTick: State<Long> = frameState
        }
    }
}