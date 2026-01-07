package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Velocity

class BounceResolver : CollisionResolver {
    override fun resolve(e1: Entity, e2: Entity, data: CollisionData) {
        val p1 = e1.get<Position>()!!
        val v1 = e1.get<Velocity>()!!
        val p2 = e2.get<Position>()!!
        val v2 = e2.get<Velocity>()!!

        if (data.overlapX < data.overlapY) {
            val shift = data.overlapX / 2f
            if (data.diffX > 0) { p1.x += shift; p2.x -= shift } else { p1.x -= shift; p2.x += shift }
            v1.x = -v1.x
            v2.x = -v2.x
        } else {
            val shift = data.overlapY / 2f
            if (data.diffY > 0) { p1.y += shift; p2.y -= shift } else { p1.y -= shift; p2.y += shift }
            v1.y = -v1.y
            v2.y = -v2.y
        }
    }
}