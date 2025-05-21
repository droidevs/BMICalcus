package io.droidevs.bmicalc.utils



fun calculateProgressPercentage(
    currentBmi: Float,
    initialBmi: Float,
    targetBmi: Float
): Float {
    return when {
        currentBmi <= targetBmi -> {
            ((currentBmi - initialBmi) / (targetBmi - initialBmi))
        }
        else -> 100f
    }.coerceIn(0f, 1f)
}