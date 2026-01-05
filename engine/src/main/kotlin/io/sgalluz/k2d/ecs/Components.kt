package io.sgalluz.k2d.ecs

import androidx.compose.ui.graphics.Color

interface Component

data class Position(var x: Float, var y: Float) : Component

data class Velocity(var x: Float, var y: Float) : Component

data class Sprite(val color: Color, val size: Float) : Component

class PlayerInput : Component

data class BoxCollider(
    val width: Float,
    val height: Float,
    var isColliding: Boolean = false
) : Component