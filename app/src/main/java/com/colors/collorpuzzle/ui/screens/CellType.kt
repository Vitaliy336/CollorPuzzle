package com.colors.collorpuzzle.ui.screens

import androidx.compose.ui.graphics.Color

enum class CellType {
    RED_CELL {
        override val color: Int = 1
        override val colorValue: Color = Color.Red
        override val colorName : String = "Red"
    },
    GREEN_CELL {
        override val color: Int = 2
        override val colorValue: Color = Color.Green
        override val colorName : String = "Green"
    },
    BLUE_CELL {
        override val color: Int = 3
        override val colorValue: Color = Color.Blue
        override val colorName : String = "BLUE"
    },
    YELLOW_CELL {
        override val color: Int = 4
        override val colorValue: Color = Color.Yellow
        override val colorName : String = "Yellow"
    },
    BARRIER_CELL {
        override val color: Int = -1
        override val colorValue: Color = Color.Black
        override val colorName : String = "Barrier"
    };

    abstract val color: Int
    abstract val colorValue: Color
    abstract val colorName : String
}
