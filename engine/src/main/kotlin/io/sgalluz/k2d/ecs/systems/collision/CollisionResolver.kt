package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.Entity

interface CollisionResolver {
    fun resolve(e1: Entity, e2: Entity, data: CollisionData)
}