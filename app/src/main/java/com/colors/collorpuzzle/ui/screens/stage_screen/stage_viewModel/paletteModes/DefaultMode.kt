package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes

import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.PaletteAlgorithm
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.data.isSingleColorPalette
import com.colors.collorpuzzle.data.local.IPuzzleDataStore
import com.colors.collorpuzzle.data.model.Stage
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DefaultMode(
    private val stageName: String,
    private val localStorage: IPuzzleDataStore,
    private val repo: RemoteConfigRepo,
) : AbsPaletteMode() {
    private lateinit var stageData: Stage
    override lateinit var matrixToPlayWith: Matrix

    override fun initPaletteData() {
        val stages = repo.getConfigs()
        val currentStage: Stage? = stages.flatMap { it.stages }.find { it.stageName == stageName }
        if (currentStage != null) {
            stageData = currentStage
            matrixToPlayWith = stageData.stagePalette.deepMatrixCopy()
            attemptsCount = stageData.stageAttempts
            _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
                colorToPaint = stageData.colorToPaint,
                matrix = matrixToPlayWith,
                attemptsLeft = attemptsCount
            )
        } else {
            _gameScreenFlow.value = GameScreenState.Error
        }
    }

    override fun cellClick(posX: Int, posY: Int, color: Int) {
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

    override fun resetScreen() {
        matrixToPlayWith = stageData.stagePalette.deepMatrixCopy()
        attemptsCount = stageData.stageAttempts
        _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
            colorToPaint = stageData.colorToPaint,
            matrix = matrixToPlayWith,
            attemptsLeft = attemptsCount
        )
    }

    override fun onStageCleared(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            localStorage.addClearedLvl(stageName)
        }
    }
}