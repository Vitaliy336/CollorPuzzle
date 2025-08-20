package com.colors.collorpuzzle

import androidx.navigation.NavType
import androidx.navigation.navArgument

interface AppDestination {
    val route: String
}

object MainMenu : AppDestination {
    override val route: String = "main_menu"
}

object StageConstructor : AppDestination {
    override val route: String = "stage_constructor"
}

object GameScreen : AppDestination {
    override val route: String = "game_screen"
    const val stageName = "stage_name"
    val routeWithArgs = "${route}/{${stageName}}"
    val arguments = listOf(navArgument(stageName) { type = NavType.StringType })

    object GameMatrix : AppDestination {
        override val route: String = "game_matrix"
    }

    object GameDialog : AppDestination {
        override val route: String = "dialog_screen"
        const val dialogState = "dialog_state"
        const val stageName = "stage_name"
        val routeWithArgs = "${this.route}/{${dialogState}}/{${stageName}}"
        val dialogArguments = listOf(navArgument(dialogState) {
            type = NavType.EnumType(type = DialogState::class.java)
        }, navArgument(stageName) {
            type = NavType.StringType
        })
    }
}

object CustomGameScreen : AppDestination {
    override val route: String = "custom_stage"
    const val stageData = "stage_data"
    val routeWithArgs = "${route}/{${stageData}}"
    val arguments = listOf(navArgument(stageData) { type = NavType.StringType })
}

object StageSelector : AppDestination {
    override val route: String = "stage_selector"
}

enum class DialogState {
    STAGE_CLEARED, OUT_OF_ATTEMPTS
}
