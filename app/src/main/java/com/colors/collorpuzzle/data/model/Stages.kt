package com.colors.collorpuzzle.data.model

import com.colors.collorpuzzle.data.Matrix

data class Stages(
    val stageCategoryName: String,
    val stages: List<Stage>,
)

data class Stage(
    val stageName: String,
    val stageAttempts: Int,
    val colorToPaint: Int,
    val stagePalette: Matrix,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stage

        if (stageAttempts != other.stageAttempts) return false
        if (colorToPaint != other.colorToPaint) return false
        if (stageName != other.stageName) return false
        if (!stagePalette.contentDeepEquals(other.stagePalette)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stageAttempts
        result = 31 * result + colorToPaint
        result = 31 * result + stageName.hashCode()
        result = 31 * result + stagePalette.contentDeepHashCode()
        return result
    }
}