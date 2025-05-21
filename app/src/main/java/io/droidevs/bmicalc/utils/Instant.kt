package io.droidevs.bmicalc.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Instant.format(pattern: String): String =
    DateTimeFormatter.ofPattern(pattern)
        .withZone(ZoneId.systemDefault())
        .format(this.toJavaInstant())