package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.*

class CollisionSystem : GameSystem {
    override fun update(entities: List<Entity>, deltaTime: Float) {
        entities.forEach { it.get<BoxCollider>()?.isColliding = false }

        for (i in entities.indices) {
            val e1 = entities[i]
            val p1 = e1.get<Position>() ?: continue
            val c1 = e1.get<BoxCollider>() ?: continue

            for (j in i + 1 until entities.size) {
                val e2 = entities[j]
                val p2 = e2.get<Position>() ?: continue
                val c2 = e2.get<BoxCollider>() ?: continue

                if (checkCollision(p1, c1, p2, c2)) {
                    c1.isColliding = true
                    c2.isColliding = true
                }
            }
        }
    }

    private fun checkCollision(p1: Position, c1: BoxCollider, p2: Position, c2: BoxCollider): Boolean {
        return p1.x < p2.x + c2.width &&
                p1.x + c1.width > p2.x &&
                p1.y < p2.y + c2.height &&
                p1.y + c1.height > p2.y
    }
}