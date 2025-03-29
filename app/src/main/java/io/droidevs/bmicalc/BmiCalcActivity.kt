package io.droidevs.bmicalc


import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

import io.droidevs.bmicalc.ui.theme.BmiCalcTheme

import io.droidevs.bmicalc.ui.window.LocalWindow


class BmiCalcActivity : ComponentActivity() {



    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            BmiCalcTheme {
                Content()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

}

@Composable
fun Content(){
    TODO()
}