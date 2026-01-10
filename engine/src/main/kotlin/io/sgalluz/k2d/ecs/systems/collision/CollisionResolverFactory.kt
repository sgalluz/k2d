package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.CollisionResponse

object CollisionResolverFactory {
    private val staticResolver = StaticResolver()
    private val bounceResolver = BounceResolver()
    private val pushResolver = PushResolver()

    fun getResolver(response: CollisionResponse): CollisionResolver? = when (response) {
        CollisionResponse.STATIC -> staticResolver
        CollisionResponse.BOUNCE -> bounceResolver
        CollisionResponse.PUSH -> pushResolver
        CollisionResponse.NONE -> null
    }
}