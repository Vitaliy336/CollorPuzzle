package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

sealed class ConstructorIntent {
    data class PaletteClick(val x: Int, val y: Int, val color: Int) : ConstructorIntent()
    data class UpdateSelectedColor(val color: Int) : ConstructorIntent()
    data class SelectColorToFillPalette(val color: Int) : ConstructorIntent()
    object ResetConstructor : ConstructorIntent()
    object ExportStage : ConstructorIntent()
}