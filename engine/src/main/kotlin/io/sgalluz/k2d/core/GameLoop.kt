package io.sgalluz.k2d.core

import androidx.compose.runtime.*
import kotlinx.coroutines.isActive

class GameLoop(private val onUpdate: (Float) -> Unit) {
    private val ticker = TimeTicker()

    // Internal state to trigger recomposition
    private val _frameState = mutableStateOf(0L)
    val frameState: State<Long> = _frameState

    fun update(frameTimeNanos: Long) {
        val deltaTime = ticker.tick(frameTimeNanos)
        if (deltaTime > 0f) onUpdate(deltaTime)
        _frameState.value = frameTimeNanos
    }

    companion object {
        /**
         * Creates and remembers a GameLoop instance, automatically
         * starting the frame clock connection.
         */
        @Composable
        fun rememberInstance(onUpdate: (Float) -> Unit): GameLoop {
            val gameLoop = remember { GameLoop(onUpdate) }

            LaunchedEffect(gameLoop) {
                while (isActive) {
                    withFrameNanos { frameNanos ->
                        gameLoop.update(frameNanos)
                    }
                }
            }
            return gameLoop
        }
    }
}