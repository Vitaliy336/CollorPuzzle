package com.colors.collorpuzzle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.colors.collorpuzzle.ui.screens.main_menu.composable.ShowMainMenu
import com.colors.collorpuzzle.ui.screens.stage_screen.composable.StageScreen
import com.colors.collorpuzzle.ui.theme.ColorPuzzleTheme

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
        NavHost(navController = navController, startDestination = PuzzleNavigation.MainMenu.route) {
            composable(route = PuzzleNavigation.MainMenu.route) {
                ShowMainMenu(
                    Modifier.fillMaxSize(),
                    playClicked = {
                        navController.navigate(PuzzleNavigation.GameScreen.route)
                    },
                    randomStageClicked = {
                        // TODO:
                    },
                    constructorClicked = {
                        // TODO:
                    })
            }
            composable(route = PuzzleNavigation.GameScreen.route) {
                StageScreen(Modifier)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        ColorPuzzleTheme {
            StageScreen(Modifier)
        }
    }
}