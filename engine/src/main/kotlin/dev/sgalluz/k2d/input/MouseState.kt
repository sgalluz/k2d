package dev.sgalluz.k2d.input

data class MouseState(
    var x: Float = 0f,
    var y: Float = 0f,
    var isLeftPressed: Boolean = false,
    var isRightPressed: Boolean = false,
)
