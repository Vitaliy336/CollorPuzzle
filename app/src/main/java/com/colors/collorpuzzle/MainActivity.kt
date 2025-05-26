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
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.colors.collorpuzzle.ui.screens.main_menu.composable.ShowMainMenu
import com.colors.collorpuzzle.ui.screens.stage_screen.composable.StageScreen
import com.colors.collorpuzzle.ui.screens.stage_selector.composable.StageSelectorScreen
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
                        // TODO:
                    })
            }
            composable(
                route = GameScreen.routeWithArgs,
                arguments = GameScreen.arguments
            ) { nav ->
                val stageName = nav.arguments?.getString(GameScreen.stageName) ?: ""
                StageScreen(modifier = Modifier, stageName,
                    backClick = {
                        navController.navigateUp()
                    })
            }

            composable(route = StageSelector.route) {
                StageSelectorScreen(
                    modifier = Modifier,
                    backClick = {
                        navController.navigateUp()
                    },
                    selectStageClick = { name ->
                        Log.d(TAG, "NavigationComponent: selected stage name = $name")
                        navController.navigateToSelectedStage(name)
                    })
            }
        }
    }


    private fun NavHostController.navigateToSelectedStage(stageName: String) {
        this.navigate("${GameScreen.route}/${stageName}") {
            launchSingleTop = true
        }
    }
}