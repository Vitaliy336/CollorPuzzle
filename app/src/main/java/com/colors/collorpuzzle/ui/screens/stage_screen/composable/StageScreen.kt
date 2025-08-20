package com.colors.collorpuzzle.ui.screens.stage_screen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.data.CellType
import com.colors.collorpuzzle.data.CellType.Companion.getCellColor
import com.colors.collorpuzzle.data.Matrix
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageIntent.ColorSelect
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageIntent.InitStage
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageIntent.PaletteCLicked
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageIntent.RestartStage
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageIntent.StageCleared
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageViewModel
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.paletteModes.AbsPaletteMode.GameScreenState
import com.colors.collorpuzzle.ui.shared.color_selector.ColorsPalette
import com.colors.collorpuzzle.ui.shared.control_components.ImageButtonWithTextComposable
import com.colors.collorpuzzle.ui.shared.control_components.MovesLeftComponent
import com.colors.collorpuzzle.ui.shared.stage_matrix.BuildStageMatrix
import com.colors.collorpuzzle.ui.shared.stage_matrix.ColorToComposable
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StageScreen(
    modifier: Modifier, stageName: String,
    backClick: () -> Unit,
    toDialog: (Boolean) -> Unit,
    stageData: String = "",
) {
    val vm: StageViewModel = koinViewModel<StageViewModel>() {
        parametersOf(stageName, stageData)
    }

    LaunchedEffect(key1 = Unit) {
        vm.handleIntent(InitStage)
    }
    val showDialog = rememberSaveable { mutableStateOf(true) }

    val stageScreenState = vm.gameScreenState.collectAsStateWithLifecycle()
    val selectedColor = vm.colorState.collectAsStateWithLifecycle()

    when (stageScreenState.value) {
        GameScreenState.Error -> {}
        GameScreenState.Loading -> Loading()
        is GameScreenState.UpdateGameScreen -> {

            val stageValue =
                (stageScreenState.value as GameScreenState.UpdateGameScreen)

            ShowStageScreen(
                modifier = modifier,
                colorToPaint = stageValue.colorToPaint,
                matrix = stageValue.matrix,
                attemptsCount = stageValue.attemptsLeft,
                restartClick = {
                    vm.handleIntent(RestartStage)
                },
                cellClick = { x, y, color ->
                    vm.handleIntent(PaletteCLicked(x, y, color))
                },
                selectedColor = selectedColor.value,
                selectColorClick = { color ->
                    vm.handleIntent(ColorSelect(color))
                },
                backClick = backClick
            )

            if (stageValue.isRunOfAttempts || stageValue.isCleared) {
                if (showDialog.value) {
                    showDialog.value = false
                    toDialog.invoke(stageValue.isCleared)
                    if (stageValue.isCleared) {
                        vm.handleIntent(StageCleared)
                    }
                }
            }
        }
    }
}

@Composable
private fun Loading() {
    // do nothing for now
}

@Composable
private fun ShowStageScreen(
    modifier: Modifier = Modifier,
    matrix: Matrix,
    colorToPaint: Int,
    attemptsCount: Int,
    selectedColor: Int,
    cellClick: (x: Int, y: Int, color: Int) -> Unit,
    backClick: () -> Unit,
    restartClick: () -> Unit,
    selectColorClick: (color: Int) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LEFT COMPONENT
        Box(
            modifier = modifier
                .fillMaxHeight()
                .weight(15f)
        ) {
            BackButton(
                backClick = backClick,
                modifier = modifier
                    .padding(start = 16.dp, top = 20.dp)
                    .align(Alignment.TopStart)
            )

            MovesLeftComponent(
                attempts = attemptsCount,
                modifier = modifier
                    .align(Alignment.Center)
                    .padding(bottom = 20.dp)
            )

            ImageButtonWithTextComposable(
                modifier = modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp),
                componentText = stringResource(R.string.restart_btn_text),
                painter = painterResource(R.drawable.ic_restart),
                buttonClick = restartClick
            )
        }

        // MID COMPONENT
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(70f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PalettePuzzleScreen(
                modifier, cellType = getCellColor(colorToPaint),
                matrix,
                cellClick = { x, y, color ->
                    cellClick(x, y, color)
                }
            )
        }

        // RIGHT COMPONENT
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(15f)
                .defaultMinSize(minWidth = 68.dp),
        ) {
            ColorsPalette(
                modifier = Modifier,
                selectedColor = selectedColor,
                clickListener = { color ->
                    selectColorClick(color)
                })
        }
    }
}

@Composable
private fun PalettePuzzleScreen(
    modifier: Modifier,
    cellType: CellType,
    matrix: Matrix,
    cellClick: (x: Int, y: Int, color: Int) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .weight(8f)
                .fillMaxWidth()
        ) {
            ColorToComposable(cellType = cellType, modifier = modifier)
        }

        BoxWithConstraints(
            modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp,
                    bottom = 0.dp,
                    top = 0.dp
                )
                .weight(70f),
            contentAlignment = Alignment.Center,
        ) {
            val boxWithConstraintsScope = this
            BuildStageMatrix(
                modifier = modifier,
                boxWithConstraintsScope.maxWidth,
                boxWithConstraintsScope.minHeight,
                matrix,
                cellClick = cellClick
            )
        }
    }
}

@Composable
private fun BackButton(
    backClick: () -> Unit,
    modifier: Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.close),
        contentDescription = "back",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(32.dp)
            .clickable {
                backClick.invoke()
            }
    )
}

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun StageScreenPreview() {
    ShowStageScreen(
        modifier = Modifier,
        restartClick = {},
        cellClick = { x, y, color -> },
        colorToPaint = CellType.GREEN_CELL.colorValue,
        attemptsCount = 3,
        selectColorClick = {},
        selectedColor = 0,
        backClick = {},
        matrix = arrayOf(
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        )
    )
}