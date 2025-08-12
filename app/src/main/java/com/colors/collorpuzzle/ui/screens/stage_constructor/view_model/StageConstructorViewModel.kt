package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

import androidx.lifecycle.ViewModel
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.PaletteAlgorithm
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.data.model.Stage
import com.colors.collorpuzzle.ui.screens.stage_constructor.ConstructorIntent
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

private const val custom_constructor_name = "custom_stage"

class StageConstructorViewModel : ViewModel() {
    private val initialConstructorMatrix = arrayOf(
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2),
        intArrayOf(-2, -2, -2, -2, -2, -2, -2, -2, -2, -2)
    )

    private var matrixToPlayWith: Matrix = initialConstructorMatrix.deepMatrixCopy()

    private val _saveStageState = MutableStateFlow<StageSaveState>(StageSaveState())
    private val _selectedColorState = MutableStateFlow(-2)
    private val _colorToFillPalette = MutableStateFlow(0)
    private val _constructorState: MutableStateFlow<Matrix> =
        MutableStateFlow<Matrix>(
            matrixToPlayWith
        )

    val selectedColor: StateFlow<Int> = _selectedColorState
    val constructorStateFlow: StateFlow<Matrix> = _constructorState
    val colorToFillPalette: StateFlow<Int> = _colorToFillPalette
    val saveStageFlow: StateFlow<StageSaveState> = _saveStageState

    fun handleIntent(constructorIntent: ConstructorIntent) {
        when (constructorIntent) {
            is ConstructorIntent.PaletteClick -> updatePalette(
                x = constructorIntent.x,
                y = constructorIntent.y,
                cellColor = constructorIntent.color
            )

            is ConstructorIntent.SelectColorToFillPalette -> setColorToFillPalette(constructorIntent.color)
            is ConstructorIntent.UpdateSelectedColor -> setSelectedColor(constructorIntent.color)
            ConstructorIntent.ResetConstructor -> resetConstructorPalette()
            ConstructorIntent.SaveStage -> saveStage()
        }
    }

    private fun saveStage() {
        when {
            colorToFillPalette.value == 0 -> _saveStageState.value =
                StageSaveState(errorType = ErrorType.FILL_COLOR_MISSING.name)

            PaletteAlgorithm.hasEmptyCells(matrixToPlayWith) -> _saveStageState.value =
                StageSaveState(errorType = ErrorType.EMPTY_PALETTE.name)

            PaletteAlgorithm.hasOtherColors(matrixToPlayWith) -> _saveStageState.value =
                StageSaveState(errorType = ErrorType.SINGLE_COLOR.name)

            else -> {
                val constructorStageData = Stage(
                    stageName = custom_constructor_name,
                    stageAttempts = Int.MAX_VALUE,
                    colorToPaint = colorToFillPalette.value,
                    stagePalette = matrixToPlayWith
                )
                val type = object : TypeToken<Stage>() {}.type
                val stageJson: String = Gson().toJson(constructorStageData, type)
                _saveStageState.value = StageSaveState(
                    stageJson = stageJson
                )
            }
        }
    }

    private fun updatePalette(x: Int, y: Int, cellColor: Int) {
        if (cellColor == selectedColor.value) return // no need to pain cell in the same color
        if (selectedColor.value == -2) return // color is not selected ignoring a call

        matrixToPlayWith = matrixToPlayWith.deepMatrixCopy().apply {
            this[y][x] = selectedColor.value
        }
        _constructorState.update {
            matrixToPlayWith
        }
    }

    private fun resetConstructorPalette() {
        matrixToPlayWith = initialConstructorMatrix.deepMatrixCopy()
        _constructorState.update {
            matrixToPlayWith
        }
    }

    private fun setColorToFillPalette(color: Int) {
        _colorToFillPalette.value = color
    }

    private fun setSelectedColor(color: Int) {
        _selectedColorState.value = color
    }
}
