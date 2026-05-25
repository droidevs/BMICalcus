package io.droidevs.bmicalc.domain.model

enum class ChartType {
     BMI("BMI"),
    HEIGHT("Height"),
    WEIGHT("Weight");

    val text: String

    constructor(text: String){
        this.text = text
    }
    companion object {
        fun fromText(text: String): ChartType? {
            for (chartType in values()) {
                if (chartType.text == text) {
                    return chartType
                }
            }
            return null
        }
    }
}