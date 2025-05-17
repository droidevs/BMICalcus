package io.droidevs.bmicalc.domain.model


data class Order(
    val orderBy : FilterType,
    val orderType: OrderType
)