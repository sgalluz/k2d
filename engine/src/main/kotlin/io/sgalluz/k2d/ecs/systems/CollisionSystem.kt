package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.systems.collision.CollisionManifold
import io.sgalluz.k2d.ecs.systems.collision.CollisionResponseDispatcher
import kotlin.math.abs

class CollisionSystem : GameSystem {
    private val collisionResponseDispatcher = CollisionResponseDispatcher()

    override fun update(
        entities: List<Entity>,
        deltaTime: Float,
    ) {
        entities.forEach { it.get<BoxCollider>()?.isColliding = false }
        val collidables = entities.filter { it.has<Position>() && it.has<BoxCollider>() }

        for (i in collidables.indices) {
            for (j in i + 1 until collidables.size) {
                val e1 = collidables[i]
                val e2 = collidables[j]

                val data = getCollisionManifold(e1, e2) ?: continue

                e1.get<BoxCollider>()!!.isColliding = true
                e2.get<BoxCollider>()!!.isColliding = true

                collisionResponseDispatcher.dispatch(e1, e2, data)
            }
        }
    }

    private fun getCollisionManifold(
        e1: Entity,
        e2: Entity,
    ): CollisionManifold? {
        val p1 = e1.get<Position>()!!
        val c1 = e1.get<BoxCollider>()!!
        val p2 = e2.get<Position>()!!
        val c2 = e2.get<BoxCollider>()!!

        val deltaX = (p1.x + c1.width / 2f) - (p2.x + c2.width / 2f)
        val deltaY = (p1.y + c1.height / 2f) - (p2.y + c2.height / 2f)
        val overlapX = (c1.width + c2.width) / 2f - abs(deltaX)
        val overlapY = (c1.height + c2.height) / 2f - abs(deltaY)

        if (overlapX <= 0 || overlapY <= 0) return null

        return CollisionManifold(overlapX, overlapY, deltaX, deltaY)
    }
}
