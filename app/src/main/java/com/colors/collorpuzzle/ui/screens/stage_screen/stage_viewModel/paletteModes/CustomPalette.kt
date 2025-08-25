package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes

import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.PaletteAlgorithm
import com.colors.collorpuzzle.data.convertToStage
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.data.isSingleColorPalette
import com.colors.collorpuzzle.data.model.Stage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class CustomPalette(
    val stageJson: String,
    val gson: Gson,
) : AbsPaletteMode() {
    override lateinit var matrixToPlayWith: Matrix
    private lateinit var stageData: Stage

    override fun initPaletteData() {
        val stage = stageJson.convertToStage(gson)
        if (stage != null) {
            stageData = stage
            matrixToPlayWith = stageData.stagePalette.deepMatrixCopy()
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
            attemptsCount++
            if (matrixToPlayWith.isSingleColorPalette(
                    color = stageData.colorToPaint
                )
            ) {
                _gameScreenFlow.update {
                    it as GameScreenState.UpdateGameScreen
                    it.copy(isCleared = true)
                }
            } else {
                _gameScreenFlow.update {
                    it as GameScreenState.UpdateGameScreen
                    it.copy(
                        matrix = matrixToPlayWith,
                        attemptsLeft = attemptsCount
                    )
                }
            }
        }
    }

    override fun resetScreen() {
        matrixToPlayWith = stageData.stagePalette.deepMatrixCopy()
        attemptsCount = 0
        _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
            colorToPaint = stageData.colorToPaint,
            matrix = matrixToPlayWith,
            attemptsLeft = attemptsCount
        )
    }

    override fun onStageCleared(coroutineScope: CoroutineScope) {
        // no need to save smth
    }
}