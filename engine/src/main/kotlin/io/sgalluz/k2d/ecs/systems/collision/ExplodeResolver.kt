package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.BoxCollider
import io.sgalluz.k2d.ecs.CollisionResponse
import io.sgalluz.k2d.ecs.DeletionMark
import io.sgalluz.k2d.ecs.Entity
import io.sgalluz.k2d.ecs.Position
import io.sgalluz.k2d.ecs.Velocity

class ExplodeResolver : CollisionResolver {
    private val explosionPower = 500f

    override fun resolve(e1: Entity, e2: Entity, manifold: CollisionManifold) {
        if (e1.get<BoxCollider>()?.response == CollisionResponse.EXPLODE) e1.add(DeletionMark())
        if (e2.get<BoxCollider>()?.response == CollisionResponse.EXPLODE) e2.add(DeletionMark())

        val res1 = e1.get<BoxCollider>()?.response
        val res2 = e2.get<BoxCollider>()?.response

        if (res1 == CollisionResponse.EXPLODE && res2 != CollisionResponse.EXPLODE) {
            applyPush(victim = e2, bomb = e1)
        } else if (res2 == CollisionResponse.EXPLODE && res1 != CollisionResponse.EXPLODE) {
            applyPush(victim = e1, bomb = e2)
        }
    }

    private fun applyPush(victim: Entity, bomb: Entity) {
        val vVel = victim.get<Velocity>() ?: return
        val vPos = victim.get<Position>() ?: return
        val bPos = bomb.get<Position>() ?: return
        val vCol = victim.get<BoxCollider>() ?: return
        val bCol = bomb.get<BoxCollider>() ?: return

        val vCenterX = vPos.x + vCol.width / 2f
        val bCenterX = bPos.x + bCol.width / 2f

        vVel.x = if (vCenterX > bCenterX) explosionPower else -explosionPower
    }
}