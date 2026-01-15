package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.CollisionResponse

object CollisionResolverFactory {
    private val resolvers =
        mapOf(
            CollisionResponse.STATIC to StaticResolver(),
            CollisionResponse.BOUNCE to BounceResolver(),
            CollisionResponse.PUSH to PushResolver(),
            CollisionResponse.EXPLODE to ExplodeResolver(),
        )

    fun getResolver(response: CollisionResponse) = resolvers[response]
}
