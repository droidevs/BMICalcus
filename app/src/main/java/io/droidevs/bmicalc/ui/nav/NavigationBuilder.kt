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
import io.droidevs.bmicalc.ui.helper.actions.BmiCalculatorScreenAction
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordDetailsAction
import io.droidevs.bmicalc.ui.helper.actions.BmiRecordEditAction
import io.droidevs.bmicalc.ui.helper.actions.HistoryScreenAction
import io.droidevs.bmicalc.ui.helper.event.BmiCalculatorScreenEvent
import io.droidevs.bmicalc.ui.helper.event.BmiRecordDetailsEvent
import io.droidevs.bmicalc.ui.helper.event.BmiRecordEditScreenEvent
import io.droidevs.bmicalc.ui.helper.event.BmiRecordHistoryScreenEvent
import io.droidevs.bmicalc.ui.layouts.SomethingWrongLayout
import io.droidevs.bmicalc.ui.nav.navigators.Navigator
import io.droidevs.bmicalc.ui.nav.navigators.rememberHomeNavigator
import io.droidevs.bmicalc.ui.nav.roots.Graph

import io.droidevs.bmicalc.ui.nav.roots.Screen
import io.droidevs.bmicalc.ui.screens.BMIScreen
import io.droidevs.bmicalc.ui.screens.BmiEditRecordScreen
import io.droidevs.bmicalc.ui.screens.BmiHistoryScreen
import io.droidevs.bmicalc.ui.screens.BmiRecordDetailsScreen
import io.droidevs.bmicalc.ui.screens.GoalSetupScreen
import io.droidevs.bmicalc.ui.snackbar.SnackBarController
import io.droidevs.bmicalc.ui.snackbar.SnackBarEvent
import io.droidevs.bmicalc.ui.utils.ObserveAsEvents
import io.droidevs.bmicalc.ui.viewmodels.BMICalculatorViewModel
import io.droidevs.bmicalc.ui.viewmodels.BmiGoalSetupViewModel
import io.droidevs.bmicalc.ui.viewmodels.BmiRecordDetailsViewModel
import io.droidevs.bmicalc.ui.viewmodels.BmiRecordEditViewModel
import io.droidevs.bmicalc.ui.viewmodels.BmiRecordHistoryViewModel


fun NavGraphBuilder.homeNavHost(
    gotoSettings: () -> Unit,
    toggleDrawer: () -> Unit
){
    composable<Graph.Home> {
        val navigator = rememberHomeNavigator()
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ){
            calculatorScreen(
                gotoSettings = gotoSettings,
                toggleDrawer = toggleDrawer,
                navigator = navigator
            )
            goalSetupScreen(
                navigator = navigator
            )
        }
    }
}


fun NavGraphBuilder.chartScreen(){
    composable<Screen.Chart> {
        //BmiChartScreen()
    }
}

fun NavGraphBuilder.calculatorScreen(
    navigator: Navigator,
    gotoSettings: () -> Unit,
    toggleDrawer: () -> Unit
){
    composable<Screen.Calculator> {
        val viewModel : BMICalculatorViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()
        ObserveAsEvents(
            flow = viewModel.event
        ) { event ->
            when(event){
                is BmiCalculatorScreenEvent.NavigateToGoalSetUpScreen -> {
                    navigator.navigateTo(Screen.GoalSetup)
                }

                null -> {}
            }
        }

        if (state.value.error != null){
            SomethingWrongLayout(
                errorMessage = "Something went wrong on our end please report it to us",
                onRetry = {
                    viewModel.onAction(BmiCalculatorScreenAction.ReloadData)
                }
            )
        } else {
            BMIScreen(
                state = state.value,
                onAction = viewModel::onAction,
                goToSettings = gotoSettings,
                toggleDrawer = toggleDrawer
            )
        }
    }
}


@Composable
fun NavGraphBuilder.historyScreen(
    navigator: Navigator,
    onDrawerMenuClick: () -> Unit,
    onSettingsClick: () -> Unit
){
    composable<Screen.History>{
        val viewModel : BmiRecordHistoryViewModel = hiltViewModel()
        val state = viewModel.state.collectAsStateWithLifecycle()
        ObserveAsEvents(
            flow = viewModel.event
        ) { event ->
            when(event){
                is BmiRecordHistoryScreenEvent.NavigateBack -> {
                    navigator.navigateUp()
                }
                is BmiRecordHistoryScreenEvent.NavigateToFavorites -> {
                    navigator.navigateTo(Screen.Favorites)
                }
                else -> {
                    event.toMessageResId()?.let { message ->
                        SnackBarController.sendEvent(
                            SnackBarEvent(
                                message = message
                            )
                        )
                    }
                }
            }
        }

        if (state.value.error != null){
            SomethingWrongLayout(
                errorMessage = stringResource(R.string.history_failed_to_load),
                onRetry = {
                    viewModel.onAction(HistoryScreenAction.Refresh)
                }
            )
        } else {
            BmiHistoryScreen(
                state = state.value,
                onAction = viewModel::onAction,
                onDrawerMenuClick = onDrawerMenuClick,
                onSettingsClick = onSettingsClick
            )
        }
    }
}


fun NavGraphBuilder.editRecordScreen(
    navigator: Navigator
) {
    composable<Screen.EditRecord> {
        val args = it.toRoute<Screen.EditRecord>()
        val viewModel: BmiRecordEditViewModel = hiltViewModel()
        viewModel.recordId = args.recordId
        val state = viewModel.state.collectAsStateWithLifecycle()
        ObserveAsEvents(
            flow = viewModel.eventSharedFlow
        ) { event ->
            when (event) {
                is BmiRecordEditScreenEvent.SavedSuccessfully -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = event.toMessageRes()!!
                        )
                    )
                    navigator.navigateUp()
                }

                else -> {
                    event.toMessageRes()?.let { message ->
                        SnackBarController.sendEvent(
                            SnackBarEvent(
                                message = message
                            )
                        )
                    }
                }
            }
        }

        if (state.value.error != null) {
            SomethingWrongLayout(
                errorMessage = stringResource(R.string.record_failed_to_load),
                onCancel = {
                    navigator.navigateUp()
                },
                onRetry = {
                    viewModel.onAction(BmiRecordEditAction.ReloadData)
                }
            )
        } else {
            BmiEditRecordScreen(
                state = state.value,
                onAction = viewModel::onAction
            )
        }
    }
}

fun NavGraphBuilder.goalSetupScreen(
    navigator: Navigator
) {
    composable<Screen.GoalSetup> {
        val viewmodel: BmiGoalSetupViewModel = hiltViewModel()
        // todo : handle events
        GoalSetupScreen(
            state = viewmodel.state.collectAsStateWithLifecycle().value,
            onAction = viewmodel::onAction
        )
    }
}

fun NavGraphBuilder.recordDetailScreen(
    navigator: Navigator
) {
    composable<Screen.RecordDetail> {
        val args = it.toRoute<Screen.RecordDetail>()
        val viewModel: BmiRecordDetailsViewModel = hiltViewModel()
        viewModel.recordId = args.recordId

        val state = viewModel.state.collectAsStateWithLifecycle()
        ObserveAsEvents(
            flow = viewModel.eventSharedFlow
        ) { event ->
            when (event) {
                is BmiRecordDetailsEvent.NavigateToEditPage -> {
                    navigator.navigateTo(Screen.EditRecord(event.recordId))
                }

                is BmiRecordDetailsEvent.DeletedSuccessfully -> {
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = R.string.delete_success
                        )
                    )
                    navigator.navigateUp()
                }

                else -> {
                    event.toMessageRes()?.let { message ->
                        SnackBarController.sendEvent(
                            SnackBarEvent(
                                message = message
                            )
                        )
                    }
                }
            }
        }
        if (state.value.error != null) {
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

fun BmiRecordHistoryScreenEvent.toMessageResId(): Int? = when (this) {
    BmiRecordHistoryScreenEvent.RecordDeletedSuccessfully -> R.string.record_deleted_successfully
    BmiRecordHistoryScreenEvent.RecordDeleteFailed -> R.string.record_delete_failed
    else -> null
}


fun BmiRecordEditScreenEvent.toMessageRes(): Int? {
    return when (this) {
        BmiRecordEditScreenEvent.SavedSuccessfully -> R.string.bmi_saved_successfully
        BmiRecordEditScreenEvent.DataRestoredSuccessfully -> R.string.bmi_data_restored_successfully
        BmiRecordEditScreenEvent.DataRestoreFailed -> R.string.bmi_data_restore_failed
        BmiRecordEditScreenEvent.SaveFailed -> R.string.bmi_save_failed
        BmiRecordEditScreenEvent.NavigateBack -> null // No snackbar message
    }
}

fun BmiRecordDetailsEvent.toMessageRes(): Int? {
    return when (this) {
        is BmiRecordDetailsEvent.FavoredSuccessfully -> R.string.favored_success
        is BmiRecordDetailsEvent.UnfavoredSuccessfully -> R.string.unfavored_success
        is BmiRecordDetailsEvent.DeletedSuccessfully -> R.string.delete_success
        is BmiRecordDetailsEvent.FailedToDelete -> R.string.delete_error
        is BmiRecordDetailsEvent.FailedToFavorite -> R.string.favorite_error
        is BmiRecordDetailsEvent.FailedToUnfavored -> R.string.unfavorite_error
        is BmiRecordDetailsEvent.NavigateToEditPage -> null // No snackbar for navigation
        else -> null
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