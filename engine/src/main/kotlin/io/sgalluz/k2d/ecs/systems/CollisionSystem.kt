package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*
import kotlin.math.abs

class CollisionSystem : GameSystem {
    override fun update(entities: List<Entity>, deltaTime: Float) {
        entities.forEach { it.get<BoxCollider>()?.isColliding = false }
        val collidables = entities.filter { it.has<Position>() && it.has<BoxCollider>() }

        for (i in collidables.indices) {
            for (j in i + 1 until collidables.size) {
                val e1 = collidables[i]
                val e2 = collidables[j]

                if (checkAABB(e1, e2)) {
                    e1.get<BoxCollider>()?.isColliding = true
                    e2.get<BoxCollider>()?.isColliding = true

                    resolve(e1, e2)
                }
            }
        }
    }

    private fun resolve(e1: Entity, e2: Entity) {
        val c1 = e1.get<BoxCollider>()!!
        val c2 = e2.get<BoxCollider>()!!

        when {
            c1.response == CollisionResponse.STATIC && c2.response != CollisionResponse.STATIC ->
                pushOut(mobile = e2, obstacle = e1)

            c2.response == CollisionResponse.STATIC && c1.response != CollisionResponse.STATIC ->
                pushOut(mobile = e1, obstacle = e2)

            // FIXME: add handler for BOUNCE CollisionResponse
        }
    }

    private fun pushOut(mobile: Entity, obstacle: Entity) {
        val mPos = mobile.get<Position>()!!
        val mCol = mobile.get<BoxCollider>()!!
        val oPos = obstacle.get<Position>()!!
        val oCol = obstacle.get<BoxCollider>()!!

        // 1. Let's calculate the centers of the two colliders
        val mCenterX = mPos.x + mCol.width / 2f
        val mCenterY = mPos.y + mCol.height / 2f
        val oCenterX = oPos.x + oCol.width / 2f
        val oCenterY = oPos.y + oCol.height / 2f

        // 2. Distance between centers on both axes
        val diffX = mCenterX - oCenterX
        val diffY = mCenterY - oCenterY

        // 3. Sum of the semi-dimensions (the minimum distance to avoid touching)
        val minDistanceX = (mCol.width + oCol.width) / 2f
        val minDistanceY = (mCol.height + oCol.height) / 2f

        // 4. Let's calculate the effective overlap
        val overlapX = minDistanceX - abs(diffX)
        val overlapY = minDistanceY - abs(diffY)

        // 5. We solve on the axis of minor overlap
        if (overlapX < overlapY) {
            // We move the mobile to the left or right of the obstacle
            mPos.x += if (diffX > 0) overlapX else -overlapX
        } else {
            // We move the mobile above or below the obstacle
            mPos.y += if (diffY > 0) overlapY else -overlapY
        }
    }

    private fun checkAABB(e1: Entity, e2: Entity): Boolean {
        val p1 = e1.get<Position>()!!
        val c1 = e1.get<BoxCollider>()!!
        val p2 = e2.get<Position>()!!
        val c2 = e2.get<BoxCollider>()!!

        return p1.x < p2.x + c2.width &&
                p1.x + c1.width > p2.x &&
                p1.y < p2.y + c2.height &&
                p1.y + c1.height > p2.y
    }
}