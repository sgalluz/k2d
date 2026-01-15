package io.sgalluz.k2d.core

class GameLoop(
    private val ticker: TimeTicker = TimeTicker(),
    private val onUpdate: (Float) -> Unit,
) {
    fun update(frameTimeNanos: Long) {
        val deltaTime = ticker.tick(frameTimeNanos)
        if (deltaTime > 0f) onUpdate(deltaTime)
    }
}
