package com.colors.collorpuzzle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.colors.collorpuzzle.ui.screens.main_menu.composable.ShowMainMenu
import com.colors.collorpuzzle.ui.screens.stage_constructor.StageConstructor.StageConstructorScreen
import com.colors.collorpuzzle.ui.screens.stage_screen.composable.StageScreen
import com.colors.collorpuzzle.ui.screens.stage_selector.composable.StageSelectorScreen
import com.colors.collorpuzzle.ui.shared.stage_dialog.PuzzleDialog
import com.colors.collorpuzzle.ui.theme.ColorPuzzleTheme

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorPuzzleTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationComponent()
                }
            }
        }
    }

    @Composable
    fun NavigationComponent() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = MainMenu.route) {
            composable(route = MainMenu.route) {
                ShowMainMenu(
                    Modifier.fillMaxSize(),
                    playClicked = {
                        navController.navigate(StageSelector.route)
                    },
                    randomStageClicked = {
                        // TODO:
                    },
                    constructorClicked = {
                        navController.navigate(StageConstructor.route)
                    })
            }

            composable(route = StageConstructor.route) {
                StageConstructorScreen(backClick = {
                    navController.navigateUp()
                })
            }

            navigation(
                startDestination = GameScreen.GameMatrix.route,
                route = GameScreen.routeWithArgs,
                arguments = GameScreen.arguments
            ) {

                composable(route = GameScreen.GameMatrix.route) { entry ->
                    val parentEntry =
                        remember(entry) { navController.getBackStackEntry(GameScreen.routeWithArgs) }
                    val stageName: String =
                        parentEntry.arguments?.getString(GameScreen.stageName) ?: ""
                    val isFromConstructor: Boolean =
                        parentEntry.arguments?.getBoolean(GameScreen.constructor) == true
                    StageScreen(
                        modifier = Modifier, stageName = stageName,
                        backClick = {
                            navController.navigateUp()
                        },
                        isFromConstructor = isFromConstructor,
                        toDialog = { isCleared ->
                            navController.openDialog(if (isCleared) DialogState.STAGE_CLEARED else DialogState.OUT_OF_ATTEMPTS)
                        })
                }

                dialog(
                    route = GameScreen.GameDialog.routeWithArgs,
                    arguments = GameScreen.GameDialog.dialogArguments
                ) { entry ->
                    val parentEntry =
                        remember(entry) { navController.getBackStackEntry(GameScreen.GameDialog.routeWithArgs) }
                    val isCleared = parentEntry.arguments?.get(GameScreen.GameDialog.dialogState)

                    ShowDialog(
                        isStageCleared = isCleared == DialogState.STAGE_CLEARED,
                        confirmClick = {
                            navController.popBackStack(StageSelector.route, inclusive = false)
                        },
                        dismissClick = {
                            // TODO double check
                            navController.navigate(GameScreen.GameMatrix.route) {
                                popUpTo(GameScreen.GameMatrix.route) { inclusive = true }
                            }
                        }
                    )
                }
            }

            composable(route = StageSelector.route) {
                StageSelectorScreen(
                    modifier = Modifier,
                    backClick = {
                        navController.navigateUp()
                    },
                    selectStageClick = { name ->
                        Log.d(TAG, "NavigationComponent: selected stage name = $name")
                        navController.navigateToSelectedStage(name, true)
                    })
            }
        }
    }

    @Composable
    fun ShowDialog(
        isStageCleared: Boolean,
        confirmClick: () -> Unit,
        dismissClick: () -> Unit,
    ) {
        PuzzleDialog(
            dialogTitle = if (isStageCleared) stringResource(R.string.dialog_stage_cleared_msg) else
                stringResource(R.string.dialog_stage_failed_msg),
            isSingleButtonDialog = isStageCleared,
            confirmRequest = confirmClick,
            dismissRequest = dismissClick,
            confirmBtnText = stringResource(R.string.dialog_btn_to_menu),
            dismissBtnText = if (isStageCleared) "" else stringResource(R.string.dialog_btn_restart)
        )
    }

    private fun NavHostController.navigateToSelectedStage(
        stageName: String,
        constructor: Boolean = false,
    ) {
        this.navigate("${GameScreen.route}/${stageName}/${constructor}") {
            launchSingleTop = true
        }
    }

    private fun NavHostController.openDialog(dialogState: DialogState) {
        this.navigate("${GameScreen.GameDialog.route}/${dialogState}") {
            launchSingleTop = true
        }
    }
}