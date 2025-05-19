package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

import android.util.Log


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
            showGrid(grid)
            Log.e("!TAG", "floodFill: ", )
            helper(
                grid = grid,
                sRow = posX,
                sCell = posY,
                newColor = newColorValue,
                color = oldColor,
                visitedBranches = hashSetOf()
            )

            showGrid(grid)
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

        fun showGrid(grid: Array<IntArray>) {
            grid.forEach {
                print("[")
                it.forEach {
                    print("$it, ")
                }
                print("]")
                println()
            }
        }


    }

}