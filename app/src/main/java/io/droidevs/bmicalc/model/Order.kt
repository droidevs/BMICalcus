package io.droidevs.bmicalc.model


data class Order(
    val orderBy : FilterType,
    val orderType: OrderType
)