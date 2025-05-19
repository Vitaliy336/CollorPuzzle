package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

sealed class StageIntent {
    data class InitStage(val name: String) : StageIntent()
    data class PaletteCLicked(val x: Int, val y: Int, val color: Int) : StageIntent()
    object RestartStage : StageIntent()
    data class ColorSelect(val color: Int) : StageIntent()
}