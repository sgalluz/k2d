package io.sgalluz.k2d.ecs.systems

import io.sgalluz.k2d.ecs.Entity

/**
 * Base interface for all game systems.
 * Systems process entities that match specific component requirements.
 */
interface GameSystem {
    /**
     * Called every frame to update the system logic.
     * @param entities List of all entities in the world.
     * @param deltaTime Time elapsed since the last frame in seconds.
     */
    fun update(entities: List<Entity>, deltaTime: Float)
}