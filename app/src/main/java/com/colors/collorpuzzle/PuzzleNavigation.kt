package com.colors.collorpuzzle

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    val route: String
}

object MainMenu : AppDestination {
    override val route: String = "main_menu"
}

object GameScreen : AppDestination {
    override val route: String = "game_screen"
    const val stageName = "stage_name"
    val routeWithArgs = "${route}/{${stageName}}"
    val arguments = listOf(navArgument(stageName) { type = NavType.StringType })
}

object StageSelector : AppDestination {
    override val route: String = "stage_selector"
}
