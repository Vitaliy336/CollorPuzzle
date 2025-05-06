package com.colors.collorpuzzle.ui.screens.stage_screen.composable

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.ui.screens.CellType
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.Matrix

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

@Composable
fun StageScreen(modifier: Modifier) {

    val context = LocalContext.current
    var attemptsCount by remember { mutableIntStateOf(7) }

    var selectedColor by remember {
        mutableStateOf(-1L)
    }

    Row(
        modifier = modifier
            .fillMaxSize(),
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
                Text(text = "$attemptsCount")
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
                            attemptsCount = 7
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
                modifier, cellType = CellType.GREEN_CELL, cellClick = {
                    attemptsCount -= 1
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
                    Toast.makeText(context, "$color", Toast.LENGTH_SHORT).show()
                    selectedColor = color
                })
        }
    }
}

@Composable
fun PalettePuzzleScreen(modifier: Modifier, cellType: CellType, cellClick: () -> Unit) {
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
                temp_matrix,
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
    StageScreen(Modifier)
}