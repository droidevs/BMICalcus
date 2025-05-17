package io.droidevs.bmicalc.domain.model

import androidx.compose.ui.graphics.Color

enum class BMICategory(val displayName: String, val color: Color) {
    UNDERWEIGHT("Underweight", Color(0xFF2196F3)),
    NORMAL("Normal", Color(0xFF4CAF50)),
    OVERWEIGHT("Overweight", Color(0xFFFFC107)),
    OBESE("Obese", Color(0xFFF44336));

    companion object {
        fun getCategory(bmi: Float): BMICategory {
            return when {
                bmi < 18.5 -> UNDERWEIGHT
                bmi < 25 -> NORMAL
                bmi < 30 -> OVERWEIGHT
                else -> OBESE
            }
        }
    }
}

fun getCategory(bmi: Float) : BMICategory {
    return BMICategory.getCategory(bmi)
}