package io.sgalluz.k2d.ecs.systems.collision

data class CollisionManifold(
    val overlapX: Float,
    val overlapY: Float,
    val deltaX: Float,
    val deltaY: Float
)