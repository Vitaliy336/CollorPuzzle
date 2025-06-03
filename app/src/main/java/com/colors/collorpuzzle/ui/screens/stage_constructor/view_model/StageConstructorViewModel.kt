package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

import androidx.lifecycle.ViewModel
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.ui.screens.stage_constructor.ConstructorIntent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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
    private val _selectedColorState = MutableStateFlow(-2)
    private val _colorToFillPalette = MutableStateFlow(0)
    private val _constructorState: MutableStateFlow<Matrix> =
        MutableStateFlow<Matrix>(
            matrixToPlayWith
        )

    val selectedColor: StateFlow<Int> = _selectedColorState
    val constructorStateFlow: StateFlow<Matrix> = _constructorState
    val colorToFillPalette: StateFlow<Int> = _colorToFillPalette

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

    fun updatePalette(x: Int, y: Int, cellColor: Int) {
        if (cellColor == selectedColor.value) return // no need to pain cell in the same color
        if (selectedColor.value == -2) return // color is not selected ignoring a call

        matrixToPlayWith = matrixToPlayWith.deepMatrixCopy().apply {
            this[y][x] = selectedColor.value
        }
        _constructorState.update {
            matrixToPlayWith
        }
    }

    fun resetConstructorPalette() {
        matrixToPlayWith = initialConstructorMatrix.deepMatrixCopy()
        _constructorState.update {
            matrixToPlayWith
        }
    }

    fun setColorToFillPalette(color: Int) {
        _colorToFillPalette.value = color
    }

    fun setSelectedColor(color: Int) {
        _selectedColorState.value = color
    }
}
