package com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes

import com.colors.collorpuzzle.data.Matrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class AbsPaletteMode() {
    abstract var matrixToPlayWith: Matrix
    protected val _selectedColor = MutableStateFlow<Int>(0)
    protected val _gameScreenFlow =
        MutableStateFlow<GameScreenState>(GameScreenState.Loading)
    protected var attemptsCount: Int = 0

    val colorState: StateFlow<Int> = _selectedColor
    val gameScreenState: StateFlow<GameScreenState> = _gameScreenFlow

    fun updateSelectedColor(color: Int) {
        _selectedColor.value = color
    }

    abstract fun initPaletteData()
    abstract fun cellClick(posX: Int, posY: Int, color: Int)
    abstract fun resetScreen()
    abstract fun onStageCleared(coroutineScope: CoroutineScope)


    sealed class GameScreenState {
        data class UpdateGameScreen(
            val colorToPaint: Int,
            val matrix: Matrix,
            val attemptsLeft: Int,
            val isCleared: Boolean = false,
            val isRunOfAttempts: Boolean = false,
        ) : GameScreenState()

        object Loading : GameScreenState()
        object Error : GameScreenState()
    }
}