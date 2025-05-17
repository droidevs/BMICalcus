package io.droidevs.bmicalc.domain.model

enum class OrderType(
    val text: String,
) {
    ASC("ASC"),
    DESC("DESC"),
    None("ASC")
}