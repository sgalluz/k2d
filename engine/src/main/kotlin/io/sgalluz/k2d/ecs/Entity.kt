package io.sgalluz.k2d.ecs

import kotlin.reflect.KClass

class Entity(val id: Int) {
    @PublishedApi
    internal val components = mutableMapOf<KClass<*>, Any>()

    fun <T : Any> add(component: T): Entity {
        components[component::class] = component
        return this
    }

    inline fun <reified T : Any> get(): T? = components[T::class] as? T

    inline fun <reified T : Component> has(): Boolean = components.containsKey(T::class)
}
