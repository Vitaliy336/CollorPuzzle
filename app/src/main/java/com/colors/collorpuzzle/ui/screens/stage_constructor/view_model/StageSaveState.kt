package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

data class StageSaveState(val stageJson: String = "", val errorType: String = "")

enum class ErrorType {
    SINGLE_COLOR, EMPTY_PALETTE, FILL_COLOR_MISSING
}

