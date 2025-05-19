package io.droidevs.bmicalc

import android.content.Context
import android.content.pm.ConfigurationInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import io.droidevs.bmicalc.ui.snackbar.SnackBarController
import io.droidevs.bmicalc.ui.theme.BmiCalcTheme
import io.droidevs.bmicalc.ui.utils.ObserveAsEvents
import io.droidevs.bmicalc.ui.utils.ObserveAsEventsCompose

import io.droidevs.bmicalc.ui.window.LocalWindow
import io.droidevs.bmicalc.ui.window.WindowInfo
import io.droidevs.bmicalc.ui.window.calculateWindowSize
import io.droidevs.bmicalc.ui.window.getFoldableInfo
import io.droidevs.bmicalc.ui.window.rememberWindowInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BmiCalcActivity : ComponentActivity() {

    private var windowInfoFlow = mutableStateOf(WindowInfo())

    val windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    var windowInfoTracker : WindowInfoTracker? = null

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        windowInfoTracker = WindowInfoTracker.getOrCreate(this)

        // Collect folding changes
        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                windowInfoTracker!!.windowLayoutInfo(this@BmiCalcActivity)
                    .collect { layoutInfo ->
                        val foldingFeature = layoutInfo.displayFeatures.firstOrNull() as FoldingFeature
                        windowInfoFlow.value = windowInfoFlow.value.copy(foldableInfo = getFoldableInfo(foldingFeature))
                    }
            }
        }

        setContent {
            CompositionLocalProvider(LocalWindow provides windowInfoFlow.value) {
                BmiCalcTheme {
                    val snackbarHostState = remember {
                        SnackbarHostState()
                    }
                    val scope = rememberCoroutineScope()
                    ObserveAsEventsCompose(
                        flow = SnackBarController.events,
                        snackbarHostState
                    ) { event ->
                        val message = stringResource(event.message)
                        LaunchedEffect(
                            true
                        ) {
                            snackbarHostState.currentSnackbarData?.dismiss()
                            val result = snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = event.action?.name,
                                duration = SnackbarDuration.Long
                            )
                            if (result == SnackbarResult.ActionPerformed){
                                event.action?.onAction?.invoke()
                            }
                        }
                    }
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState
                            )
                        }
                    ) { paddingValues ->
                        Content(paddingValues)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateWindowSize()
    }

    private fun updateWindowSize() {
        windowInfoFlow.value = windowInfoFlow.value.copy(windowSize = calculateWindowSize(resources.displayMetrics.density,this))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}

@Composable
fun Content(paddingValues: PaddingValues){
    TODO()
}