package dev.sgalluz.k2d.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import dev.sgalluz.k2d.runtime.compose.LocalGameLoopClock
import dev.sgalluz.k2d.runtime.compose.rememberGameLoop

@Composable
fun k2dProvideGameLoop(
    onUpdate: (Float) -> Unit,
    gameLoopProvider: @Composable ((Float) -> Unit) -> GameLoopClock = { rememberGameLoop(it) },
    content: @Composable () -> Unit,
) {
    val gameLoop = gameLoopProvider(onUpdate)

    CompositionLocalProvider(
        LocalGameLoopClock provides gameLoop,
    ) {
        content()
    }
}
