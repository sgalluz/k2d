package io.sgalluz.k2d.runtime.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import io.sgalluz.k2d.core.GameLoop
import io.sgalluz.k2d.runtime.GameLoopClock
import kotlinx.coroutines.isActive

@Composable
fun rememberGameLoop(onUpdate: (Float) -> Unit): GameLoopClock {
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
