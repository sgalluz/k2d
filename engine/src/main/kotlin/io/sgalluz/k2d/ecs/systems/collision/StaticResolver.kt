package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position

class StaticResolver : CollisionResolver {
    override fun resolve(e1: Entity, e2: Entity, data: CollisionData) {
        val c1 = e1.get<BoxCollider>()!!

        val isE1Mobile = c1.response != CollisionResponse.STATIC
        val mobilePos = if (isE1Mobile) e1.get<Position>()!! else e2.get<Position>()!!
        val multiplier = if (isE1Mobile) 1f else -1f

        if (data.overlapX < data.overlapY) {
            val sign = if (data.diffX > 0) 1f else -1f
            mobilePos.x += data.overlapX * sign * multiplier
        } else {
            val sign = if (data.diffY > 0) 1f else -1f
            mobilePos.y += data.overlapY * sign * multiplier
        }
    }
}