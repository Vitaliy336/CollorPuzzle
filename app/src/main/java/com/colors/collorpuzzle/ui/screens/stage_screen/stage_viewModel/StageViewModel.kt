package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.data.local.IPuzzleDataStore
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.ui.screens.stage_constructor.stage_constructor.customPalette
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes.CustomPalette
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes.DefaultMode
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes.AbsPaletteMode
import com.google.gson.Gson

class StageViewModel(
    private val repo: RemoteConfigRepo,
    private val localStorage: IPuzzleDataStore,
    private val gson: Gson,
    private val stageName: String,
    private val stageData: String,
) : ViewModel() {

    private val paletteMode: AbsPaletteMode by lazy {
        when (stageName) {
            customPalette ->
                CustomPalette(
                    stageJson = stageData,
                    gson = gson
                )

            else -> DefaultMode(stageName = stageName, localStorage = localStorage, repo = repo)
        }
    }

    val gameScreenState = paletteMode.gameScreenState
    val colorState = paletteMode.colorState

    fun handleIntent(stageIntent: StageIntent) {
        when (stageIntent) {
            is StageIntent.ColorSelect -> updateSelectedColor(stageIntent.color)
            is StageIntent.PaletteCLicked -> cellClick(
                stageIntent.x,
                stageIntent.y,
                stageIntent.color
            )

            is StageIntent.InitStage -> initStage()
            StageIntent.RestartStage -> resetStage()
            StageIntent.StageCleared -> stageCleared()
        }
    }

    private fun updateSelectedColor(color: Int) {
        paletteMode.updateSelectedColor(color)
    }

    private fun initStage() {
        paletteMode.initPaletteData()
    }

    private fun resetStage() {
        paletteMode.resetScreen()
    }

    private fun cellClick(posX: Int, posY: Int, color: Int) {
        paletteMode.cellClick(posX = posX, posY = posY, color = color)
    }

    private fun stageCleared() {
        paletteMode.onStageCleared(viewModelScope)
    }
}