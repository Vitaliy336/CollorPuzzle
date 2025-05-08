package com.colors.collorpuzzle

sealed class PuzzleNavigation(val route: String) {
    object MainMenu: PuzzleNavigation("main_menu")
    object GameScreen: PuzzleNavigation("game_screen")
    object StageSelector: PuzzleNavigation("stage_selector")
}