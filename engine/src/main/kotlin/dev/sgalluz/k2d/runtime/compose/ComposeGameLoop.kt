package dev.sgalluz.k2d.runtime.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import dev.sgalluz.k2d.core.GameLoop
import dev.sgalluz.k2d.runtime.GameLoopClock
import kotlinx.coroutines.NonCancellable.isActive

suspend fun runGameLoop(
    gameLoop: GameLoop,
    emitFrame: (Long) -> Unit,
    frameProvider: suspend ((Long) -> Unit) -> Unit,
) {
    frameProvider { frame ->
        gameLoop.update(frame)
        emitFrame(frame)
    }
}

@Composable
fun rememberGameLoop(
    onUpdate: (Float) -> Unit,
    enabled: Boolean = true,
): GameLoopClock {
    val gameLoop = remember { GameLoop(onUpdate = onUpdate) }
    val frameState = remember { mutableStateOf(0L) }

    if (enabled) {
        LaunchedEffect(Unit) {
            // FIXME: code smell... Remove asap!!!
            while (isActive) {
                runGameLoop(
                    gameLoop = gameLoop,
                    emitFrame = { frameState.value = it },
                    frameProvider = { onFrame ->
                        withFrameNanos { onFrame(it) }
                    },
                )
            }
        }
    }

    return remember {
        object : GameLoopClock {
            override val frameTick: State<Long> = frameState
        }
    }
}
