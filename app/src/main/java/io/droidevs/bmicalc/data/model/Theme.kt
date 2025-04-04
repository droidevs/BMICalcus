package io.droidevs.bmicalc.data.model

enum class Theme(
    val themeName: String
) {
    DARK("dark"),
    LIGHT("light"),
    SYSTEM("system");

    companion object {
        fun fromString(themeName: String): Theme {
            return values().first { it.themeName == themeName }
        }
    }
}