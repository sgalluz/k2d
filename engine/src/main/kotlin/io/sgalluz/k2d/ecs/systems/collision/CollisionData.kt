package io.sgalluz.k2d.ecs.systems.collision

data class CollisionData(
    val overlapX: Float,
    val overlapY: Float,
    val diffX: Float,
    val diffY: Float
)