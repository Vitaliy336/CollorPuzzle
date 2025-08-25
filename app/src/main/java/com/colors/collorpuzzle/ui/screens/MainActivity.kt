package com.colors.collorpuzzle.ui.screens

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
import com.colors.collorpuzzle.CustomGameScreen
import com.colors.collorpuzzle.DialogState
import com.colors.collorpuzzle.GameScreen
import com.colors.collorpuzzle.MainMenu
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.StageConstructor
import com.colors.collorpuzzle.StageSelector
import com.colors.collorpuzzle.ui.screens.main_menu.composable.ShowMainMenu
import com.colors.collorpuzzle.ui.screens.stage_constructor.stage_constructor.StageConstructorScreen
import com.colors.collorpuzzle.ui.screens.stage_constructor.stage_constructor.customPalette
import com.colors.collorpuzzle.ui.screens.stage_screen.composable.StageScreen
import com.colors.collorpuzzle.ui.screens.stage_selector.composable.StageSelectorScreen
import com.colors.collorpuzzle.ui.shared.stage_dialog.PuzzleDialog
import com.colors.collorpuzzle.ui.theme.ColorPuzzleTheme


private const val TAG = "MainActivity"
const val randomPalette = "random_palette"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColorPuzzleTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
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
                    Modifier.Companion.fillMaxSize(),
                    playClicked = {
                        navController.navigate(StageSelector.route)
                    },
                    randomStageClicked = {
                        navController.navigateToCustomGameScreen(stageName = randomPalette)
                    },
                    constructorClicked = {
                        navController.navigate(StageConstructor.route)
                    },
                    launchCustomStage = {
                        navController.navigateToCustomGameScreen(it, customPalette)
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
                    val stageName = parentEntry.arguments?.getString(GameScreen.stageName) ?: ""
                    StageScreen(
                        modifier = Modifier.Companion,
                        stageName = stageName,
                        backClick = {
                            navController.navigateUp()
                        },
                        toDialog = { isCleared ->
                            navController.openDialog(
                                if (isCleared) DialogState.STAGE_CLEARED else DialogState.OUT_OF_ATTEMPTS,
                                stageName
                            )
                        })
                }

                dialog(
                    route = GameScreen.GameDialog.routeWithArgs,
                    arguments = GameScreen.GameDialog.dialogArguments
                ) { entry ->
                    val parentEntry =
                        remember(entry) { navController.getBackStackEntry(GameScreen.GameDialog.routeWithArgs) }
                    val isCleared = parentEntry.arguments?.get(GameScreen.GameDialog.dialogState)
                    val stageName = parentEntry.arguments?.get(GameScreen.GameDialog.stageName)

                    ShowDialog(
                        isStageCleared = isCleared == DialogState.STAGE_CLEARED,
                        confirmClick = {
                            val destinationRoute =
                                if (stageName == customPalette) MainMenu.route else StageConstructor.route
                            navController.popBackStack(destinationRoute, inclusive = false)
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
                    modifier = Modifier.Companion,
                    backClick = {
                        navController.navigateUp()
                    },
                    selectStageClick = { name ->
                        Log.d(TAG, "NavigationComponent: selected stage name = $name")
                        navController.navigateToSelectedStage(name)
                    })
            }

            composable(
                route = CustomGameScreen.routeWithArgs,
                arguments = CustomGameScreen.arguments
            ) { entry ->
                val parentEntry =
                    remember(entry) { navController.getBackStackEntry(CustomGameScreen.routeWithArgs) }
                val stageData = parentEntry.arguments?.getString(CustomGameScreen.stageData) ?: ""
                val stageName = parentEntry.arguments?.getString(CustomGameScreen.stageName) ?: ""
                StageScreen(
                    modifier = Modifier.Companion,
                    stageName = stageName,
                    backClick = {
                        navController.navigateUp()
                    },
                    toDialog = {
                        navController.openDialog(
                            DialogState.STAGE_CLEARED,
                            stageName = customPalette
                        )
                    },
                    stageData = stageData
                )
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

    private fun NavHostController.navigateToCustomGameScreen(stageName:String, paletteData: String = "") {
        this.navigate("${CustomGameScreen.route}/${paletteData}/${stageName}") {
            launchSingleTop = true
        }
    }

    private fun NavHostController.navigateToSelectedStage(stageName: String) {
        this.navigate("${GameScreen.route}/${stageName}") {
            launchSingleTop = true
        }
    }

    private fun NavHostController.openDialog(dialogState: DialogState, stageName: String) {
        this.navigate("${GameScreen.GameDialog.route}/${dialogState}/${stageName}") {
            launchSingleTop = true
        }
    }
}