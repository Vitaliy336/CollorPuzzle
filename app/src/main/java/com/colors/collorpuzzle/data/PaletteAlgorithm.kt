package com.colors.collorpuzzle.data

import com.colors.collorpuzzle.ui.screens.CellType


typealias Matrix = Array<IntArray>

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
                color == CellType.BARRIER_CELL.color -> return // TODO double check barrier handling
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

        fun showGrid(grid: Array<IntArray>) { // print matrix
            grid.forEach {
                print("[")
                it.forEach {
                    print("$it, ")
                }
                print("]")
                println()
            }
        }

        fun isSingleColorPalette(color: Int, grid: Matrix): Boolean =
            !grid.any { intArr -> intArr.any { it != color } }

    }
}

fun Matrix.deepMatrixCopy(): Matrix =
    Array(this.size) { index ->
        this[index].copyOf()
    }