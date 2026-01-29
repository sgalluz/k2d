package dev.sgalluz.k2d.ecs.systems.collision

import dev.sgalluz.k2d.ecs.Entity
import dev.sgalluz.k2d.ecs.Position
import dev.sgalluz.k2d.ecs.Velocity

class BounceResolver : CollisionResolver {
    override fun resolve(
        e1: Entity,
        e2: Entity,
        manifold: CollisionManifold,
    ) {
        val position1 = e1.get<Position>()!!
        val velocity1 = e1.get<Velocity>()!!
        val position2 = e2.get<Position>()!!
        val velocity2 = e2.get<Velocity>()!!

        if (manifold.isXAxis) {
            val direction = if (manifold.deltaX > 0f) 1f else -1f
            val separation = manifold.overlapX * 0.5f * direction
            position1.x += separation
            position2.x -= separation
            velocity1.x = -velocity1.x
            velocity2.x = -velocity2.x
        } else {
            val direction = if (manifold.deltaY > 0f) 1f else -1f
            val separation = manifold.overlapY * 0.5f * direction
            position1.y += separation
            position2.y -= separation
            velocity1.y = -velocity1.y
            velocity2.y = -velocity2.y
        }
    }
}
