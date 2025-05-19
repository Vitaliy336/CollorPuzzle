package com.colors.collorpuzzle.ui.screens.stage_screen.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.ui.screens.CellType
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.Matrix
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageIntent
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StageScreen(modifier: Modifier, stageName: String) {
    val vm = koinViewModel<StageViewModel>()
    vm.handleIntent(StageIntent.InitStage(stageName))

    val matrix = vm.paletteFlow.collectAsState()
    val attemptsCount = vm.paintAttemptsFlow.collectAsState()

    val stageScreenState = vm.gameScreenFlow.collectAsState()
    when (stageScreenState.value) {
        StageViewModel.GameScreenState.Error -> TODO()
        StageViewModel.GameScreenState.Loading -> TODO()
        is StageViewModel.GameScreenState.Ready -> {
            val colorToPaint =
                (stageScreenState.value as StageViewModel.GameScreenState.Ready).colorToPaint
            ShowStageScreen(
                modifier = modifier,
                colorToPaint = colorToPaint,
                matrix = matrix.value,
                attemptsCount = attemptsCount,
                restartClick = { vm.handleIntent(StageIntent.RestartStage) },
                cellClick = { x, y, color ->
                    vm.handleIntent(StageIntent.PaletteCLicked(x, y, color))
                }
            )
        }

        StageViewModel.GameScreenState.Lost -> TODO()
        StageViewModel.GameScreenState.StageCleared -> TODO()
    }
}

@Composable
fun ShowStageScreen(
    modifier: Modifier = Modifier,
    colorToPaint: Int,
    cellClick: (x: Int, y: Int, color: Int) -> Unit,
    restartClick: () -> Unit,
    matrix: StageViewModel.PaletteState,
    attemptsCount: State<Int>,
) {

    var selectedColor by remember {
        mutableIntStateOf(0)
    }
    Row(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(15f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .weight(50f)
            ) {
                Text(text = "Remaining moves")
                Text(text = "${attemptsCount.value}")
            }
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .weight(50f)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_restart),
                    contentDescription = "restart",
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .padding(16.dp)
                        .size(56.dp)
                        .border(2.dp, color = Color.Gray, shape = CircleShape)
                        .clip(shape = CircleShape)
                        .background(color = Color.White)
                        .clickable {
                            restartClick()
                        }
                )
                Text(text = "Restart")
            }
        }

        Column(
            modifier = modifier
                .fillMaxHeight()
                .weight(70f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PalettePuzzleScreen(
                modifier, cellType = getCellColor(colorToPaint),
                matrix.matrix,
                cellClick = { x, y, color ->
                    cellClick(x, y, color)
                }
            )
        }
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
                    selectedColor = color
                })
        }
    }
}

@Composable
fun PalettePuzzleScreen(
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
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp, top = 20.dp, bottom = 0.dp,
                ),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Gray)) {
                        append("Turn all the blocks into ")
                    }
                    withStyle(style = SpanStyle(color = cellType.colorValue)) {
                        append(cellType.colorName)
                    }
                }
            )
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

@Preview(
    name = "Landscape Mode",
    showBackground = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun StageScreenPreview() {
    val temp_matrix: Matrix = arrayOf(
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
    )

    PalettePuzzleScreen(
        modifier = Modifier,
        cellType = CellType.BLUE_CELL,
        matrix = temp_matrix,
        cellClick = { x, y, color -> })
}