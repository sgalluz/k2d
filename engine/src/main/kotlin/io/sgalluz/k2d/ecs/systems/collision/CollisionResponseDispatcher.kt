package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Velocity

class CollisionResponseDispatcher {
    fun dispatch(e1: Entity, e2: Entity, collisionManifold: CollisionManifold) {
        val r1 = e1.get<BoxCollider>()!!.response
        val r2 = e2.get<BoxCollider>()!!.response

        when {
            r1 == CollisionResponse.STATIC || r2 == CollisionResponse.STATIC -> {
                getResolver(CollisionResponse.STATIC).resolve(e1, e2, collisionManifold)
                applyPostResolutionEffects(e1, e2, collisionManifold)
            }

            r1 == CollisionResponse.BOUNCE && r2 == CollisionResponse.BOUNCE ->
                getResolver(CollisionResponse.BOUNCE).resolve(e1, e2, collisionManifold)

            r1 == CollisionResponse.PUSH || r2 == CollisionResponse.PUSH ->
                getResolver(CollisionResponse.PUSH).resolve(e1, e2, collisionManifold)
        }
    }

    private fun applyPostResolutionEffects(e1: Entity, e2: Entity, collisionManifold: CollisionManifold) {
        val mobile = if (e1.get<BoxCollider>()?.response != CollisionResponse.STATIC) e1 else e2
        val collider = mobile.get<BoxCollider>() ?: return

        if (collider.response == CollisionResponse.BOUNCE) {
            val velocity = mobile.get<Velocity>() ?: return
            val isXAxis = collisionManifold.overlapX < collisionManifold.overlapY
            if (isXAxis) velocity.x *= -1f else velocity.y *= -1f
        }
    }

    private fun getResolver(collisionResponse: CollisionResponse): CollisionResolver =
        CollisionResolverFactory.getResolver(collisionResponse)!!
}