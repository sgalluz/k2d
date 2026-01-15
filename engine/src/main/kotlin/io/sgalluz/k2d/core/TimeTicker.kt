package io.sgalluz.k2d.core

/**
 * Pure logic class to handle time calculations.
 * No dependencies on Compose or Coroutines.
 */
class TimeTicker {
    private var lastFrameTimeNanos: Long? = null

    /**
     * Updates the ticker with the current nano time.
     * @return The delta time in seconds, or 0f if it's the first frame.
     */
    fun tick(currentNanos: Long): Float {
        val last = lastFrameTimeNanos
        lastFrameTimeNanos = currentNanos
        return if (last == null) 0f else (currentNanos - last) / 1_000_000_000f
    }
}
