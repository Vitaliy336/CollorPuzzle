package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

import androidx.lifecycle.ViewModel
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.data.CellType
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.convertToJson
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.data.hasEmptyCells
import com.colors.collorpuzzle.data.isSingleColorPalette
import com.colors.collorpuzzle.data.logMatrix
import com.colors.collorpuzzle.data.model.Stage
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


const val constructorStageName = "custom_constructor_stage"

class StageConstructorViewModel(private val gson: Gson) : ViewModel() {

    private val initialConstructorMatrix by lazy {
        Array(8) { IntArray(10) { CellType.EMPTY_CELL.colorValue } } // default matrix 8 rows and 10 columns maybe add custom sizes in future
    }

    private var matrixToPlayWith: Matrix = initialConstructorMatrix.deepMatrixCopy()

    private val _constructorState =
        MutableStateFlow(
            ConstructorData(
                matrix = matrixToPlayWith,
                colorToFillPalette = CellType.EMPTY_CELL.colorValue,
                selectedColor = CellType.EMPTY_CELL.colorValue
            )
        )

    private val _exportState = MutableStateFlow<ExportState>(ExportState.Initial)

    val constructorStateFlow: StateFlow<ConstructorData> = _constructorState
    val exportState: StateFlow<ExportState> = _exportState

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
            ConstructorIntent.ExportStage -> exportStage()
        }
    }

    private fun updatePalette(x: Int, y: Int, cellColor: Int) {
        val selectedColor = _constructorState.value.selectedColor
        if (selectedColor == cellColor || selectedColor == CellType.EMPTY_CELL.colorValue) {
            // no actions needed, color is not selected or user clicked on the same color cell
            return
        } else {
            matrixToPlayWith = matrixToPlayWith.deepMatrixCopy().also {
                it[y][x] = selectedColor
            }

            _constructorState.update {
                it.copy(matrix = matrixToPlayWith)
            }
        }
    }

    private fun resetConstructorPalette() {
        matrixToPlayWith = initialConstructorMatrix.deepMatrixCopy()
        _constructorState.update {
            it.copy(matrix = matrixToPlayWith)
        }
    }

    private fun setColorToFillPalette(color: Int) {
        _constructorState.value = _constructorState.value.copy(colorToFillPalette = color)
    }

    private fun setSelectedColor(color: Int) {
        _constructorState.value = _constructorState.value.copy(selectedColor = color)
    }

    private fun exportStage() {
        val colorToPaint = _constructorState.value.colorToFillPalette
        matrixToPlayWith.logMatrix()

        when {
            colorToPaint == CellType.EMPTY_CELL.colorValue -> {
                _exportState.value =
                    ExportState.Error(error = R.string.stage_export_error_fill_color_is_not_selected)
            }

            matrixToPlayWith.hasEmptyCells() -> {
                _exportState.value =
                    ExportState.Error(error = R.string.stage_export_error_empty_cell)
            }

            matrixToPlayWith.isSingleColorPalette(matrixToPlayWith.first().first()) -> {
                _exportState.value =
                    ExportState.Error(error = R.string.stage_export_error_single_color)
            }

            else -> {
                val stage = Stage(
                    stageName = constructorStageName,
                    stageAttempts = -1,
                    colorToPaint = _constructorState.value.colorToFillPalette,
                    stagePalette = _constructorState.value.matrix
                )
                val stageJson: String = stage.convertToJson(gson)
                _exportState.value = ExportState.Success(json = stageJson)
            }
        }
    }

    sealed class ExportState() {
        object Initial : ExportState()
        data class Error(val error: Int) : ExportState()
        data class Success(val json: String) : ExportState()
    }
}
