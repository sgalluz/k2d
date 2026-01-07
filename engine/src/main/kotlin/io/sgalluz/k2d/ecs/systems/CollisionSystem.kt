package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*
import io.sgalluz.k2d.ecs.systems.collision.*
import kotlin.math.abs

class CollisionSystem : GameSystem {
    private val staticResolver = StaticResolver()
    private val bounceResolver = BounceResolver()

    override fun update(entities: List<Entity>, deltaTime: Float) {
        entities.forEach { it.get<BoxCollider>()?.isColliding = false }
        val collidables = entities.filter { it.has<Position>() && it.has<BoxCollider>() }

        for (i in collidables.indices) {
            for (j in i + 1 until collidables.size) {
                val e1 = collidables[i]
                val e2 = collidables[j]

                val data = getCollisionData(e1, e2) ?: continue

                e1.get<BoxCollider>()!!.isColliding = true
                e2.get<BoxCollider>()!!.isColliding = true

                dispatchResolution(e1, e2, data)
            }
        }
    }

    private fun getCollisionData(e1: Entity, e2: Entity): CollisionData? {
        val p1 = e1.get<Position>()!!
        val c1 = e1.get<BoxCollider>()!!
        val p2 = e2.get<Position>()!!
        val c2 = e2.get<BoxCollider>()!!

        val diffX = (p1.x + c1.width / 2f) - (p2.x + c2.width / 2f)
        val diffY = (p1.y + c1.height / 2f) - (p2.y + c2.height / 2f)
        val overlapX = (c1.width + c2.width) / 2f - abs(diffX)
        val overlapY = (c1.height + c2.height) / 2f - abs(diffY)

        if (overlapX <= 0 || overlapY <= 0) return null

        return CollisionData(overlapX, overlapY, diffX, diffY)
    }

    private fun dispatchResolution(e1: Entity, e2: Entity, data: CollisionData) {
        val r1 = e1.get<BoxCollider>()!!.response
        val r2 = e2.get<BoxCollider>()!!.response

        when {
            r1 == CollisionResponse.STATIC || r2 == CollisionResponse.STATIC ->
                staticResolver.resolve(e1, e2, data)
            r1 == CollisionResponse.BOUNCE && r2 == CollisionResponse.BOUNCE ->
                bounceResolver.resolve(e1, e2, data)
        }
    }
}