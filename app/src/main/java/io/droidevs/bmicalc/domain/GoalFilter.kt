package io.droidevs.bmicalc.domain

import io.droidevs.bmicalc.domain.model.OrderType


data class GoalFilter(
    val query : String?,
    val flag: GoalFlag?,
    val order : OrderType = OrderType.DESC
)