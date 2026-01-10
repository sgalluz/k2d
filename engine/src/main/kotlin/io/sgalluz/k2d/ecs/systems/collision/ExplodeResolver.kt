package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.DeletionMark
import io.sgalluz.k2d.ecs.Entity

class ExplodeResolver : CollisionResolver {
    override fun resolve(e1: Entity, e2: Entity, manifold: CollisionManifold) {
        val c1 = e1.get<BoxCollider>()!!
        val c2 = e2.get<BoxCollider>()!!

        if (c1.response == CollisionResponse.EXPLODE) e1.add(DeletionMark())
        if (c2.response == CollisionResponse.EXPLODE) e2.add(DeletionMark())
    }
}