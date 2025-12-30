package io.sgalluz.k2d.ecs

/**
 * Basic components for 2D movement and rendering.
 */

data class Position(var x: Float, var y: Float)

data class Velocity(var x: Float, var y: Float)

data class Sprite(
    val color: androidx.compose.ui.graphics.Color,
    val size: Float
)