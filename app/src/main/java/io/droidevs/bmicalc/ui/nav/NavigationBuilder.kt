package io.droidevs.bmicalc.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import io.droidevs.bmicalc.R
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordDetailsAction
import io.droidevs.bmicalc.ui.helper.event.BmiRecordDetailsEvent
import io.droidevs.bmicalc.ui.layouts.SomethingWrongLayout
import io.droidevs.bmicalc.ui.nav.navigators.Navigator
import io.droidevs.bmicalc.ui.nav.navigators.rememberHomeNavigator
import io.droidevs.bmicalc.ui.nav.roots.Graph

import io.droidevs.bmicalc.ui.nav.roots.Screen
import io.droidevs.bmicalc.ui.screens.BmiRecordDetailsScreen
import io.droidevs.bmicalc.ui.screens.GoalSetupScreen
import io.droidevs.bmicalc.ui.snackbar.SnackBarController
import io.droidevs.bmicalc.ui.snackbar.SnackBarEvent
import io.droidevs.bmicalc.ui.utils.ObserveAsEvents
import io.droidevs.bmicalc.ui.viewmodels.BmiGoalSetupViewModel
import io.droidevs.bmicalc.ui.viewmodels.BmiRecordDetailsViewModel


fun NavGraphBuilder.homeNavHost(){
    composable<Graph.Home> {
        val navState = rememberHomeNavigator()
        NavHost(
            navController = navState.navController,
            startDestination = navState.startDestination,
        ){
            calculatorScreen()
            goalSetupScreen()
        }
    }
}

fun NavGraphBuilder.goalSetupScreen(){
    composable<Screen.GoalSetup> {
        val viewmodel : BmiGoalSetupViewModel = hiltViewModel()
        GoalSetupScreen(
            state = viewmodel.state.collectAsStateWithLifecycle().value,
            onAction = viewmodel::onAction
        )
    }
}

fun NavGraphBuilder.recordDetailScreen(
    navigator: Navigator
){
    composable<Screen.RecordDetail> {
        val args = it.toRoute<Screen.RecordDetail>()
        val viewModel : BmiRecordDetailsViewModel = hiltViewModel()
        viewModel.recordId = args.recordId

        val state = viewModel.state.collectAsStateWithLifecycle()
        ObserveAsEvents(
            flow = viewModel.eventSharedFlow
        ) { event->
            when(event){
                is BmiRecordDetailsEvent.NavigateToEditPage -> {
                    navigator.navigateTo(Screen.EditRecord(event.recordId))
                }
                is BmiRecordDetailsEvent.FavoredSuccessfully -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.favored_success
                        )
                    )
                }
                is BmiRecordDetailsEvent.UnfavoredSuccessfully -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.unfavored_success
                        )
                    )
                }
                is BmiRecordDetailsEvent.DeletedSuccessfully -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.delete_success
                        )
                    )
                }
                is BmiRecordDetailsEvent.FailedToDelete -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.delete_error
                        )
                    )
                }
                is BmiRecordDetailsEvent.FailedToFavorite -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.favorite_error
                        )
                    )
                }
                is BmiRecordDetailsEvent.FailedToUnfavored -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.unfavorite_error
                        )
                    )
                }
            }
        }
        if(state.value.error != null){
            SomethingWrongLayout(
                errorMessage = stringResource(R.string.record_failed_to_load),
                onCancel = {
                    navigator.navigateUp()
                },
                onRetry = {
                    viewModel.onAction(BmiRecordDetailsAction.RefreshRecord)
                }
            )
        } else {
            BmiRecordDetailsScreen(
                state = state.value,
                onAction = viewModel::onAction
            )
        }
    }
}

@Composable
fun NavGraphBuilder.historyScreen(){
    composable<Screen.History>{
        //BmiHistoryScreen()
    }
}

fun NavGraphBuilder.calculatorScreen(){
    composable<Screen.Home> {
        //BMIScreen()
    }
}

fun NavGraphBuilder.chartScreen(){
    composable<Screen.Chart> {
        //BmiChartScreen()
    }
}




@Composable
fun <T : ViewModel> NavBackStackEntry.sharedViewModel(controller : NavHostController) : T {
    val navGraphRoute = destination.parent?.route ?: return TODO()//HiltViewModel()
    val parentEntry = remember(this) {
        controller.getBackStackEntry(navGraphRoute)
    }

    return TODO() //HiltViewModelFactory(controller.context,parentEntry)
}