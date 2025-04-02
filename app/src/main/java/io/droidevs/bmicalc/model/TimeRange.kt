package io.droidevs.bmicalc.model


import kotlinx.datetime.Instant
import java.time.LocalDate

sealed class TimeRange(
    val text: String
) {
    object Week : TimeRange("Last 7 Days")
    object Month: TimeRange("Last 30 days")

    object Year: TimeRange("Last year")

    class Custom(
        val start: Instant? = null,
        val end: Instant? = null
    ) : TimeRange("Custom")

    object All : TimeRange("All Time")

}