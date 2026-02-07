package dev.sgalluz.k2d.input

import androidx.compose.ui.input.key.Key

enum class InputAction { UP, DOWN, LEFT, RIGHT }

data class InputConfig(
    val bindings: Map<InputAction, Key> =
        mapOf(
            InputAction.UP to Key.DirectionUp,
            InputAction.DOWN to Key.DirectionDown,
            InputAction.LEFT to Key.DirectionLeft,
            InputAction.RIGHT to Key.DirectionRight,
        ),
)
