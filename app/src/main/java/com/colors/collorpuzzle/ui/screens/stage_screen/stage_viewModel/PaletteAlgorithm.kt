package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel

import com.colors.collorpuzzle.ui.screens.CellType


typealias Matrix = Array<IntArray>

class PaletteAlgorithm {

    companion object {

        fun floodFill(
            grid: Matrix,
            posX: Int,
            posY: Int,
            newColorValue: Int,
        ) {
            floodHelper(
                grid = grid,
                posX = posX,
                posY = posY,
                newColorValue = newColorValue,
                colorValue = grid[posY][posY],
                visitedBranches = hashSetOf()
            )
        }

        private fun floodHelper(
            grid: Matrix,
            posX: Int,
            posY: Int,
            newColorValue: Int,
            colorValue: Int,
            visitedBranches: HashSet<String>,
        ) {
            val rowInbounds = posX >= 0 && posX < grid.size
            val cellInbound = posY >= 0 && posY < grid[0].size
            val cellName = "$posX,$posY"

            when {
                !rowInbounds || cellInbound -> return
                visitedBranches.contains(cellName) -> return
                grid[posX][posY] != colorValue -> return
                grid[posX][posY] != CellType.BARRIER_CELL.color -> return // TODO: DOUBLE CHECK BARRIERS HANDLING
                else -> {
                    visitedBranches.add(cellName)
                    grid[posX][posY] = newColorValue
                    floodHelper(grid, posX + 1, posY, newColorValue, colorValue, visitedBranches)
                    floodHelper(grid, posX - 1, posY, newColorValue, colorValue, visitedBranches)
                    floodHelper(grid, posX, posY + 1, newColorValue, colorValue, visitedBranches)
                    floodHelper(grid, posX, posY - 1, newColorValue, colorValue, visitedBranches)
                }
            }
        }
    }

}