package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

import androidx.lifecycle.ViewModel
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.ui.screens.CellType
import com.colors.collorpuzzle.ui.screens.stage_constructor.ConstructorIntent
import com.colors.collorpuzzle.ui.shared.color_selector.ColorSelector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StageConstructorViewModel : ViewModel() {

    private val initialConstructorMatrix by lazy {
        Array(8) { IntArray(10) { CellType.EMPTY_CELL.color } } // default matrix 8 rows and 10 columns maybe add custom sizes in future
    }

    data class ConstructorData(
        val matrix: Matrix,
        val colorToFillPalette: Int,
        val selectedColor: Int,
    )

    private var matrixToPlayWith: Matrix = initialConstructorMatrix.deepMatrixCopy()

    private val _constructorState =
        MutableStateFlow(
            ConstructorData(
                matrix = matrixToPlayWith,
                colorToFillPalette = CellType.EMPTY_CELL.color,
                selectedColor = CellType.EMPTY_CELL.color
            )
        )

    val constructorStateFlow: StateFlow<ConstructorData> = _constructorState

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
            ConstructorIntent.SaveStage -> TODO()
        }
    }

    private fun updatePalette(x: Int, y: Int, cellColor: Int) {
        val selectedColor = _constructorState.value.selectedColor
        if (selectedColor == cellColor || selectedColor == CellType.EMPTY_CELL.color) {
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
}
