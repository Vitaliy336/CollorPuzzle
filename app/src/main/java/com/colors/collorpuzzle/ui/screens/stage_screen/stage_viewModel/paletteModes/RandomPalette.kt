package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes

import com.colors.collorpuzzle.data.CellType
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.data.PaletteAlgorithm
import com.colors.collorpuzzle.data.deepMatrixCopy
import com.colors.collorpuzzle.data.isSingleColorPalette
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update
import kotlin.random.Random

// I'm expecting a 8x10 matrix, so I need only from 10 to 20 color cells for each color, diff will be covered later
fun Random.getColorsCount() = this.nextInt(10, 20)


class RandomPalette(private val matrixY: Int = 8, private val matrixX: Int = 10) :
    AbsPaletteMode() {

    private val matrixSize = matrixX * matrixY
    private val initialMatrix: Matrix by lazy {
        generateMatrix()
    }
    private val paintPaletteInto: Int by lazy {
        Random.nextInt(1, CellType.getPaletteColors().size + 1) // starting with 1 because of colors representation
    }

    override lateinit var matrixToPlayWith: Matrix

    override fun initPaletteData() {
        matrixToPlayWith = initialMatrix.deepMatrixCopy()
        _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
            colorToPaint = paintPaletteInto,
            matrix = matrixToPlayWith,
            attemptsLeft = attemptsCount
        )
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
                    color = paintPaletteInto
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
        matrixToPlayWith = initialMatrix.deepMatrixCopy()
        attemptsCount = 0
        _gameScreenFlow.value = GameScreenState.UpdateGameScreen(
            colorToPaint = paintPaletteInto,
            matrix = matrixToPlayWith,
            attemptsLeft = attemptsCount
        )
    }

    override fun onStageCleared(coroutineScope: CoroutineScope) {
        // no need to save smth
    }

    private fun generateMatrix(): Matrix {
        val availableColors = CellType.getPaletteColors()
        val colorsArrayList = ArrayList<Int>()
        availableColors.forEach { cellType ->
            val colorsCount = Random.getColorsCount()
            var colorCounter = 0

            while (colorCounter <= colorsCount) {
                colorsArrayList.add(cellType.colorValue)
                colorCounter++
            }
        }
        val diff = matrixSize - (colorsArrayList.size - 1)

        if (diff > 0) {
            var randColor = 0
            var counter = 0
            while (counter < diff) {
                randColor = Random.nextInt(0, availableColors.size - 1)
                colorsArrayList.add(availableColors[randColor].colorValue)
                counter++
            }
        }

        colorsArrayList.shuffle()

        var index = 0
        val matrix = Array(matrixY) { IntArray(matrixX) }
        for (r in 0..matrixY - 1) {
            for (c in 0..matrixX - 1) {
                matrix[r][c] = colorsArrayList[index]
                index++
            }
        }
        return matrix
    }
}