package dev.sgalluz.k2d.runtime.compose

import androidx.compose.runtime.compositionLocalOf
import dev.sgalluz.k2d.runtime.GameLoopClock

val LocalGameLoopClock =
    compositionLocalOf<GameLoopClock> {
        error("GameLoopClock not provided")
    }
