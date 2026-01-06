package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*

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
                    resolveStaticCollision(e1, e2)
                }
            }
        }
    }

    private fun resolveStaticCollision(e1: Entity, e2: Entity) {
        val c1 = e1.get<BoxCollider>()!!
        val c2 = e2.get<BoxCollider>()!!

        if (c1.isStatic && c2.isStatic) return

        // FIXME: handle dynamic resolution
        if (!c1.isStatic && !c2.isStatic) return

        val mobile = if (!c1.isStatic) e1 else e2
        val static = if (c1.isStatic) e1 else e2

        val mPos = mobile.get<Position>()!!
        val mCol = mobile.get<BoxCollider>()!!
        val sPos = static.get<Position>()!!
        val sCol = static.get<BoxCollider>()!!

        val overlapX = if (mPos.x < sPos.x)
            (mPos.x + mCol.width) - sPos.x
        else
            (sPos.x + sCol.width) - mPos.x

        val overlapY = if (mPos.y < sPos.y)
            (mPos.y + mCol.height) - sPos.y
        else
            (sPos.y + sCol.height) - mPos.y

        if (overlapX < overlapY) {
            if (mPos.x < sPos.x) mPos.x -= overlapX else mPos.x += overlapX
        } else {
            if (mPos.y < sPos.y) mPos.y -= overlapY else mPos.y += overlapY
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