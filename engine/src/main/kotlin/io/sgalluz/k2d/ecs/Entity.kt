package io.sgalluz.k2d.ecs

import kotlin.reflect.KClass

/**
 * An Entity is essentially a container for Components.
 * It uses a unique ID to identify itself within the World.
 */
class Entity(val id: Int) {
    @PublishedApi
    internal val components = mutableMapOf<KClass<*>, Any>()

    /**
     * Adds a component to this entity.
     */
    fun <T : Any> add(component: T): Entity {
        components[component::class] = component
        return this
    }

    /**
     * Reified extension for a cleaner syntax: entity.get<Position>()
     */
    inline fun <reified T : Any> get(): T? = components[T::class] as? T

    /**
     * Checks if the entity has all the specified component types.
     */
    inline fun <reified T : Component> has(): Boolean = components.containsKey(T::class)
}