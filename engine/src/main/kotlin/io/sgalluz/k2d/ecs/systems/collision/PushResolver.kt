package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position

class PushResolver : CollisionResolver {
    override fun resolve(
        e1: Entity,
        e2: Entity,
        manifold: CollisionManifold,
    ) {
        val p1 = e1.get<Position>()!!
        val p2 = e2.get<Position>()!!

        val shift = (manifold.primaryOverlap / 2f) * manifold.directionSign

        if (manifold.isXAxis) {
            p1.x += shift
            p2.x -= shift
        } else {
            p1.y += shift
            p2.y -= shift
        }
    }
}
