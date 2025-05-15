package io.droidevs.bmicalc.domain.model

import io.droidevs.bmicalc.domain.Range

open class BmiFilter(
    val time: TimeRange? = null,
    val bmi: Range? = null,
    val weight: Range? = null,
    val height: Range? = null,
    val order : Order? = null,
)