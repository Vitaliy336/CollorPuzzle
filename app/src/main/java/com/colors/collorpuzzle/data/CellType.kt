package com.colors.collorpuzzle.data

import androidx.compose.ui.graphics.Color

enum class CellType {
    RED_CELL {
        override val colorValue: Int = 1
        override val color: Color = Color.Companion.Red
        override val colorName: String = "Red"
    },
    GREEN_CELL {
        override val colorValue: Int = 2
        override val color: Color = Color.Companion.Green
        override val colorName: String = "Green"
    },
    BLUE_CELL {
        override val colorValue: Int = 3
        override val color: Color = Color.Companion.Blue
        override val colorName: String = "BLUE"
    },
    YELLOW_CELL {
        override val colorValue: Int = 4
        override val color: Color = Color.Companion.Yellow
        override val colorName: String = "Yellow"
    },
    BARRIER_CELL {
        override val colorValue: Int = -1
        override val color: Color = Color.Companion.Black
        override val colorName: String = "Barrier"
    },
    EMPTY_CELL {
        override val colorValue: Int = -2
        override val color: Color = Color.Companion.Gray
        override val colorName: String = "Empty_Constructor_Cell"
    };

    abstract val colorValue: Int
    abstract val color: Color
    abstract val colorName: String

    companion object {
        fun getPaletteColors() = listOf(BLUE_CELL, YELLOW_CELL, RED_CELL, GREEN_CELL)
        fun getColorPairs() = listOf(Pair(RED_CELL, GREEN_CELL), Pair(BLUE_CELL, YELLOW_CELL))

        fun getCellColor(cellType: Int): CellType {
            return when (cellType) {
                1 -> RED_CELL
                2 -> GREEN_CELL
                3 -> BLUE_CELL
                4 -> YELLOW_CELL
                -2 -> EMPTY_CELL
                else -> BARRIER_CELL
            }
        }
    }
}