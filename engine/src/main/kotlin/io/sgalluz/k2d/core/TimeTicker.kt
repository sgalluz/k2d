package io.sgalluz.k2d.core

/**
 * Pure logic class to handle time calculations.
 * No dependencies on Compose or Coroutines.
 */
class TimeTicker {
    private var lastFrameTimeNanos = 0L

    /**
     * Updates the ticker with the current nano time.
     * @return The delta time in seconds, or 0f if it's the first frame.
     */
    fun tick(currentNanos: Long): Float {
        if (lastFrameTimeNanos == 0L) {
            lastFrameTimeNanos = currentNanos
            return 0f
        }
        val delta = (currentNanos - lastFrameTimeNanos) / 1_000_000_000f
        lastFrameTimeNanos = currentNanos
        return delta
    }
}