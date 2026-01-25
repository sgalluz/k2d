package dev.sgalluz.k2d.runtime

import androidx.compose.runtime.State

interface GameLoopClock {
    /** Used only to trigger recomposition */
    val frameTick: State<Long>
}
