package io.droidevs.bmicalc.domain

import io.droidevs.bmicalc.domain.model.Order
import io.droidevs.bmicalc.domain.model.TimeRange

data class FavoredFilter(
    val query: String,
    val time: TimeRange? = null,
    val bmi: Range? = null,
    val weight: Range? = null,
    val height: Range? = null,
    val order : Order? = null,
)