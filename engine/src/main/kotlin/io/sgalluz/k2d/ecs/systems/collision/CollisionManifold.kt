package io.sgalluz.k2d.ecs.systems.collision

data class CollisionManifold(
    val overlapX: Float,
    val overlapY: Float,
    val deltaX: Float,
    val deltaY: Float,
) {
    val isXAxis = overlapX < overlapY
    val primaryOverlap = if (isXAxis) overlapX else overlapY
    val primaryDiff = if (isXAxis) deltaX else deltaY
    val directionSign = if (primaryDiff > 0) 1f else -1f
}
