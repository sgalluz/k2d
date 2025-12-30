package io.sgalluz.k2d.ecs

import kotlin.reflect.KClass

/**
 * An Entity is essentially a container for Components.
 * It uses a unique ID to identify itself within the World.
 */
class Entity(val id: Int) {
    // Map to store components using their class type as key
    private val components = mutableMapOf<KClass<*>, Any>()

    /**
     * Adds a component to this entity.
     */
    fun <T : Any> add(component: T): Entity {
        components[component::class] = component
        return this
    }

    /**
     * Retrieves a component of the specified type.
     * Returns null if the component is not found.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(type: KClass<T>): T? {
        return components[type] as? T
    }

    /**
     * Reified extension for a cleaner syntax: entity.get<Position>()
     */
    inline fun <reified T : Any> get(): T? = get(T::class)

    /**
     * Checks if the entity has all the specified component types.
     */
    fun has(types: Set<KClass<*>>): Boolean {
        return components.keys.containsAll(types)
    }
}