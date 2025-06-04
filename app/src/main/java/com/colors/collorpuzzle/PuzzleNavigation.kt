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
    const val constructor = "from_constructor"
    val routeWithArgs = "${route}/{${stageName}}/{${constructor}}"
    val arguments = listOf(
        navArgument(stageName) { type = NavType.StringType },
        navArgument(constructor) { type = NavType.BoolType
        defaultValue = false}

    )

    object GameMatrix : AppDestination {
        override val route: String = "game_matrix"
    }

    object GameDialog : AppDestination {
        override val route: String = "dialog_screen"
        const val dialogState = "dialog_state"
        val routeWithArgs = "${this.route}/{${dialogState}}"
        val dialogArguments = listOf(navArgument(dialogState) {
            type = NavType.EnumType(type = DialogState::class.java)
        })
    }
}

object StageSelector : AppDestination {
    override val route: String = "stage_selector"
}

enum class DialogState {
    STAGE_CLEARED, OUT_OF_ATTEMPTS
}
