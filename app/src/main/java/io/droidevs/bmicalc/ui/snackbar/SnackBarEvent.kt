package io.droidevs.bmicalc.ui.snackbar


import androidx.annotation.StringRes

class SnackBarEvent (
    @StringRes val message: Int,
    val action : SnackBarAction? = null
)