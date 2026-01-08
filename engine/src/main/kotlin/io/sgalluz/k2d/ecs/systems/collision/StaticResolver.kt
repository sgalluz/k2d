package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position

class StaticResolver : CollisionResolver {
    override fun resolve(e1: Entity, e2: Entity, manifold: CollisionManifold) {
        val c1 = e1.get<BoxCollider>()!!

        val isE1Mobile = c1.response != CollisionResponse.STATIC
        val mobilePos = if (isE1Mobile) e1.get<Position>()!! else e2.get<Position>()!!
        val multiplier = if (isE1Mobile) 1f else -1f

        if (manifold.overlapX < manifold.overlapY) {
            val sign = if (manifold.deltaX > 0) 1f else -1f
            mobilePos.x += manifold.overlapX * sign * multiplier
        } else {
            val sign = if (manifold.deltaY > 0) 1f else -1f
            mobilePos.y += manifold.overlapY * sign * multiplier
        }
    }
}