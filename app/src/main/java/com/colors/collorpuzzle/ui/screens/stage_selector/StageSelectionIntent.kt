package com.colors.collorpuzzle.ui.screens.stage_selector

sealed class StageSelectionIntent {
    object FetchStages: StageSelectionIntent()
}