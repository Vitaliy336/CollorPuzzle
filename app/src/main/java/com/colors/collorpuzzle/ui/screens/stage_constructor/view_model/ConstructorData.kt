package com.colors.collorpuzzle.ui.screens.stage_constructor.view_model

import com.colors.collorpuzzle.data.Matrix

data class ConstructorData(
    val matrix: Matrix,
    val colorToFillPalette: Int,
    val selectedColor: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ConstructorData

        if (colorToFillPalette != other.colorToFillPalette) return false
        if (selectedColor != other.selectedColor) return false
        if (!matrix.contentDeepEquals(other.matrix)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = colorToFillPalette
        result = 31 * result + selectedColor
        result = 31 * result + matrix.contentDeepHashCode()
        return result
    }
}