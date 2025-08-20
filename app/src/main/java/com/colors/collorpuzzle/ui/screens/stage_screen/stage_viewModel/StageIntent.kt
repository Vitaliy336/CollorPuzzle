package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

sealed class StageIntent {
    object InitStage : StageIntent()
    object RestartStage : StageIntent()
    object StageCleared : StageIntent()
    data class PaletteCLicked(val x: Int, val y: Int, val color: Int) : StageIntent()
    data class ColorSelect(val color: Int) : StageIntent()
}