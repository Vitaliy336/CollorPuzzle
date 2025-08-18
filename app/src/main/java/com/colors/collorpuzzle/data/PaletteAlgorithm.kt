package com.colors.collorpuzzle.data

import android.util.Log


typealias Matrix = Array<IntArray>


fun Matrix.hasEmptyCells(): Boolean {
    return this.any { intArray -> intArray.any { number -> number == CellType.EMPTY_CELL.colorValue } }
}

fun Matrix.isSingleColorPalette(color: Int): Boolean =
    !this.any { intArr -> intArr.any { it != color } }

fun Matrix.logMatrix() {
    Log.d("Matrix", "printing matrix...")
    this.forEach { arr ->
        Log.d("Matrix", "${arr.contentToString()},")
    }
}

class PaletteAlgorithm {
    companion object {

        fun floodFill(
            grid: Matrix,
            posX: Int,
            posY: Int,
            oldColor: Int,
            newColorValue: Int,
        ): Matrix {
            helper(
                grid = grid,
                sRow = posX,
                sCell = posY,
                newColor = newColorValue,
                color = oldColor,
                visitedBranches = hashSetOf()
            )

            return grid
        }

        private fun helper(
            grid: Array<IntArray>,
            sRow: Int,
            sCell: Int,
            newColor: Int,
            color: Int,
            visitedBranches: HashSet<String>,
        ) {
            val rowInbounds: Boolean = sRow >= 0 && sRow < grid[0].size
            val cellInbounds: Boolean = sCell >= 0 && sCell < grid.size
            val cellName = "$sRow,$sCell"

            when {
                !rowInbounds || !cellInbounds -> return
                visitedBranches.contains(cellName) -> return
                grid[sCell][sRow] != color -> return
                color == CellType.BARRIER_CELL.colorValue -> return // TODO double check barrier handling
                else -> {
                    visitedBranches.add(cellName)
                    grid[sCell][sRow] = newColor
                    helper(grid, sRow + 1, sCell, newColor, color, visitedBranches)
                    helper(grid, sRow - 1, sCell, newColor, color, visitedBranches)
                    helper(grid, sRow, sCell + 1, newColor, color, visitedBranches)
                    helper(grid, sRow, sCell - 1, newColor, color, visitedBranches)
                }
            }
        }
    }
}

fun Matrix.deepMatrixCopy(): Matrix =
    Array(this.size) { index ->
        this[index].copyOf()
    }