package io.sgalluz.k2d.ecs

import io.sgalluz.k2d.ecs.systems.GameSystem

/**
 * The World manages the lifecycle of entities and systems.
 * It is the main entry point for the game engine logic.
 */
class World {
    private val entities = mutableListOf<Entity>()
    private val logicSystems = mutableListOf<GameSystem>()


    /**
     * Creates and adds a new entity to the world.
     */
    fun createEntity(): Entity = Entity(entities.size).also { entities.add(it) }

    /**
     * Adds an existing system to the world.
     */
    fun addSystem(system: GameSystem) = logicSystems.add(system)

    /**
     * Updates all registered systems.
     */
    fun update(deltaTime: Float) {
        logicSystems.forEach { it.update(entities, deltaTime) }
    }

    /**
     * Returns a read-only list of current entities.
     */
    fun getEntities(): List<Entity> = entities
}