package io.sgalluz.k2d.ecs.systems.collision

import io.sgalluz.k2d.ecs.*
import kotlin.math.abs

class ExplodeResolver : CollisionResolver {
    private val explosionPower = 500f

    override fun resolve(e1: Entity, e2: Entity, manifold: CollisionManifold) {
        val r1 = e1.explosionResponse()
        val r2 = e2.explosionResponse()

        if (r1) e1.add(DeletionMark())
        if (r2) e2.add(DeletionMark())

        when {
            r1 && !r2 -> applyPush(victim = e2, bomb = e1)
            r2 && !r1 -> applyPush(victim = e1, bomb = e2)
        }
    }

    private fun Entity.explosionResponse() =
        get<BoxCollider>()?.response == CollisionResponse.EXPLODE

    private fun applyPush(victim: Entity, bomb: Entity) {
        val vVel = victim.get<Velocity>() ?: return
        val (vx, vy) = victim.center() ?: return
        val (bx, by) = bomb.center() ?: return

        val dx = vx - bx
        val dy = vy - by

        if (abs(dx) > abs(dy)) {
            vVel.x = explosionPower * dx.sign()
            vVel.y = 0f
        } else {
            vVel.y = explosionPower * dy.sign()
            vVel.x = 0f
        }
    }

    private fun Entity.center(): Pair<Float, Float>? {
        val pos = get<Position>() ?: return null
        val col = get<BoxCollider>() ?: return null
        return pos.x + col.width / 2f to pos.y + col.height / 2f
    }

    private fun Float.sign() = if (this > 0) 1f else -1f
}
