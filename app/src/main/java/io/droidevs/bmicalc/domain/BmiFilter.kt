package io.droidevs.bmicalc.domain

import io.droidevs.bmicalc.model.Order
import io.droidevs.bmicalc.model.TimeRange

class BmiFilter(
    val time: TimeRange? = null,
    val bmi: Range? = null,
    val weight: Range? = null,
    val height: Range? = null,
    val order : Order? = null,
)