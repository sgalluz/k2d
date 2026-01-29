package dev.sgalluz.k2d.ecs.systems.collision

import dev.sgalluz.k2d.ecs.Entity

interface CollisionResolver {
    fun resolve(
        e1: Entity,
        e2: Entity,
        manifold: CollisionManifold,
    )
}
