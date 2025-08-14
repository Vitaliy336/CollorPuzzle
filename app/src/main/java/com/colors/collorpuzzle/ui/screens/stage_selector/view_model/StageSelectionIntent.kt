package com.colors.collorpuzzle.ui.screens.stage_selector.view_model

sealed class StageSelectionIntent {
    object FetchStages: StageSelectionIntent()
}