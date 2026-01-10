package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.CollisionResponse

object CollisionResolverFactory {
    private val staticResolver = StaticResolver()
    private val bounceResolver = BounceResolver()

    fun getResolver(response: CollisionResponse): CollisionResolver? = when (response) {
        CollisionResponse.STATIC -> staticResolver
        CollisionResponse.BOUNCE -> bounceResolver
        CollisionResponse.NONE -> null
    }
}