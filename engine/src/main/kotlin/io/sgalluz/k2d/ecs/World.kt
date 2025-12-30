package io.sgalluz.k2d.ecs

/**
 * The World manages the lifecycle of entities and systems.
 * It is the main entry point for the game engine logic.
 */
class World {
    private val entities = mutableListOf<Entity>()
    private val systems = mutableListOf<GameSystem>()
    private var nextEntityId = 0

    /**
     * Creates and adds a new entity to the world.
     */
    fun createEntity(): Entity {
        val entity = Entity(nextEntityId++)
        entities.add(entity)
        return entity
    }

    /**
     * Adds an existing system to the world.
     */
    fun addSystem(system: GameSystem) = systems.add(system)

    /**
     * Updates all registered systems.
     */
    fun update(deltaTime: Float) {
        for (system in systems) {
            system.update(entities, deltaTime)
        }
    }

    /**
     * Returns a read-only list of current entities.
     */
    fun getEntities(): List<Entity> = entities
}