package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.data.local.PuzzleDataStore
import com.colors.collorpuzzle.data.model.Stage
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StageViewModel(
    private val repo: RemoteConfigRepo,
    private val localStorage: PuzzleDataStore,
) : ViewModel() {

    data class PaletteState(val matrix: Matrix)

    private lateinit var initialStageMatrix: Matrix // READ ONLY
    private var matrixToPlayWith: Matrix = arrayOf(intArrayOf())
    private var initialAttempts: Int = 0
    private var actualAttempts: Int = 0
    private var selectedColorValue: Int = 0

    private val _gameScreenFlow = MutableStateFlow<GameScreenState>(GameScreenState.Loading)

    private val _attemptsFlow = MutableStateFlow<Int>(actualAttempts)
    private val _matrixFlow = MutableStateFlow(PaletteState(arrayOf(intArrayOf())))

    val paintAttemptsFlow: StateFlow<Int> = _attemptsFlow
    val gameScreenFlow: StateFlow<GameScreenState> = _gameScreenFlow
    val paletteFlow: StateFlow<PaletteState> = _matrixFlow

    sealed class GameScreenState {
        data class Ready(val colorToPaint: Int) : GameScreenState()
        object Loading : GameScreenState()
        object Error : GameScreenState()
        object StageCleared : GameScreenState()
        object Lost : GameScreenState()
    }

    fun handleIntent(stageIntent: StageIntent) {
        when (stageIntent) {
            is StageIntent.ColorSelect -> updateSelectedColor(stageIntent.color)
            is StageIntent.PaletteCLicked -> cellClick(stageIntent.x, stageIntent.y, stageIntent.color)
            is StageIntent.InitStage -> getStageData(stageIntent.name)
            StageIntent.RestartStage -> reset()
        }
    }

    private fun updateSelectedColor(color: Int) {
        selectedColorValue = color
    }

    private fun getStageData(stageName: String) {
        val stages = repo.getConfigs()
        val stageData: Stage? = stages.flatMap { it.stages }.find { it.stageName == stageName }
        if (stageData != null) {
            initialStageMatrix = stageData.stagePalette
            matrixToPlayWith = stageData.stagePalette
            initialAttempts = stageData.stageAttempts
            actualAttempts = stageData.stageAttempts

            _matrixFlow.value = PaletteState(matrixToPlayWith)
            _attemptsFlow.value = actualAttempts
            _gameScreenFlow.value = GameScreenState.Ready(colorToPaint = stageData.colorToPaint)
        } else {
            _gameScreenFlow.value = GameScreenState.Error
        }
    }

    fun reset() {
        matrixToPlayWith = initialStageMatrix.copyOf()
        actualAttempts = initialAttempts
        _attemptsFlow.value = actualAttempts
        _matrixFlow.value = PaletteState(initialStageMatrix)
    }

    fun cellClick(posX: Int, posY: Int, color: Int,) {
        if (selectedColorValue == 0) return

        matrixToPlayWith = PaletteAlgorithm.floodFill(
            grid = matrixToPlayWith,
            posX = posX,
            posY = posY,
            oldColor = color,
            newColorValue = selectedColorValue
        )

        _matrixFlow.value = PaletteState(matrixToPlayWith)
    }

    private fun stageCleared(stageName: String) {
        viewModelScope.launch {
            localStorage.addClearedLvl(stageName)
        }
    }

}