package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.PaletteAlgorithm
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.data.isSingleColorPalette
import com.colors.collorpuzzle.data.local.PuzzleDataStore
import com.colors.collorpuzzle.data.model.Stage
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StageViewModel(
    private val repo: RemoteConfigRepo,
    private val localStorage: PuzzleDataStore,
) : ViewModel() {

    private lateinit var stageData: Stage
    private lateinit var matrixToPlayWith: Matrix
    private var attemptsCount: Int = 0
    private val _selectedColor = MutableStateFlow<Int>(0)
    private val _gameScreenFlow = MutableStateFlow<GameScreenState>(GameScreenState.Loading)

    val gameScreenFlow: StateFlow<GameScreenState> = _gameScreenFlow
    val selectedColorState: StateFlow<Int> = _selectedColor

    sealed class GameScreenState {
        data class UpdateGameScreen(
            val colorToPaint: Int,
            val matrix: Matrix,
            val attemptsLeft: Int,
            val isCleared: Boolean = false,
            val isRunOfAttempts: Boolean = false,
        ) : GameScreenState()

        object Loading : GameScreenState()
        object Error : GameScreenState()
    }

    fun handleIntent(stageIntent: StageIntent) {
        when (stageIntent) {
            is StageIntent.ColorSelect -> updateSelectedColor(stageIntent.color)
            is StageIntent.PaletteCLicked -> cellClick(
                stageIntent.x,
                stageIntent.y,
                stageIntent.color
            )

            is StageIntent.InitStage -> getStageData(stageIntent.name)
            StageIntent.RestartStage -> resetStage()
        }
    }

    private fun updateSelectedColor(color: Int) {
        _selectedColor.value = color
    }

    private fun getStageData(stageName: String) {
        val stages = repo.getConfigs()
        val stage: Stage? = stages.flatMap { it.stages }.find { it.stageName == stageName }
        if (stage != null) {
            stageData = stage
            matrixToPlayWith = stageData.stagePalette.deepMatrixCopy()
            attemptsCount = stage.stageAttempts
            _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
                colorToPaint = stageData.colorToPaint,
                matrix = matrixToPlayWith, attemptsLeft = attemptsCount
            )
        } else {
            _gameScreenFlow.value = GameScreenState.Error
        }
    }

    private fun resetStage() {
        matrixToPlayWith = stageData.stagePalette.deepMatrixCopy()
        attemptsCount = stageData.stageAttempts
        _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
            colorToPaint = stageData.colorToPaint,
            matrix = matrixToPlayWith,
            attemptsLeft = attemptsCount
        )
    }

    private fun cellClick(posX: Int, posY: Int, color: Int) {
        if (_selectedColor.value == 0 || _selectedColor.value == color) {
            // no actions needed if color is not selected or user clicked on the same color cell
            return
        } else {
            PaletteAlgorithm.floodFill(
                grid = matrixToPlayWith,
                posX = posX,
                posY = posY,
                oldColor = color,
                newColorValue = _selectedColor.value
            )
            attemptsCount--
            when {
                matrixToPlayWith.isSingleColorPalette(
                    color = stageData.colorToPaint,
                ) -> { // check matrix colors
                    _gameScreenFlow.update {
                        it as GameScreenState.UpdateGameScreen
                        it.copy(isCleared = true)
                    }
                    stageCleared(stageData.stageName)
                }

                attemptsCount < 0 -> {
                    _gameScreenFlow.update {
                        it as GameScreenState.UpdateGameScreen
                        it.copy(isRunOfAttempts = true)
                    }
                }

                else -> {
                    _gameScreenFlow.update {
                        (it as GameScreenState.UpdateGameScreen).copy(
                            matrix = matrixToPlayWith,
                            attemptsLeft = attemptsCount
                        )
                    }
                }
            }
        }
    }

    private fun stageCleared(stageName: String) {
        viewModelScope.launch {
            localStorage.addClearedLvl(stageName)
        }
    }
}