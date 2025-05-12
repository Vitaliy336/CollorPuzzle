package com.colors.collorpuzzle.ui.screens.stage_selector.view_model

data class StagesData(
    val stagesGroupName: String = "",
    val stagesList: List<StageData> = listOf(),
) {
    data class StageData(val stageName: String = "", val isCleared: Boolean = false)
}