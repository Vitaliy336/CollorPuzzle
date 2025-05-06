package com.colors.collorpuzzle.ui.screens.stage_screen.composable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.ui.screens.CellType
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.Matrix

@Composable
fun BuildStageMatrix(
    modifier: Modifier,
    containerWidth: Dp,
    containerHeight: Dp,
    stageMatrix: Matrix,
    cellClick: () -> Unit,
) {

    val cellWidth: Dp = containerWidth / 12
    val cellHeight: Dp = containerHeight / 10

    Column(
        modifier = modifier
    ) {

        for (i in stageMatrix.indices) {
            Row {
                for (j in stageMatrix[i].indices) {
                    MatrixItemBox(
                        modifier = modifier,
                        cellWidth = cellWidth,
                        cellHeight = cellHeight,
                        posX = j,
                        posY = i,
                        cellType = getCellColor(stageMatrix[i][j]),
                        itemClick = cellClick
                    )
                }
            }
        }
    }
}

@Composable
fun MatrixItemBox(
    modifier: Modifier,
    cellWidth: Dp,
    cellHeight: Dp,
    posX: Int,
    posY: Int,
    cellType: CellType,
    itemClick: () -> Unit,
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .width(cellWidth)
            .height(cellHeight)
            .padding(1.dp)
            .clip(shape = RoundedCornerShape(5.dp))
            .background(cellType.colorValue)
            .clickable {
                Toast
                    .makeText(context, "x-> $posX, y -> $posY", Toast.LENGTH_SHORT)
                    .show()
                itemClick()
            }
    )
}


fun getCellColor(cellType: Int): CellType {
    return when (cellType) {
        1 -> CellType.RED_CELL
        2 -> CellType.GREEN_CELL
        3 -> CellType.BLUE_CELL
        4 -> CellType.YELLOW_CELL
        else -> CellType.BARRIER_CELL
    }
}

@Preview(device = Devices.PIXEL_4_XL)
@Composable
fun BuildStageMatrixPreview() {
    BuildStageMatrix(
        Modifier, 1200.dp, 1449.dp, arrayOf(
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
            intArrayOf(1, 2, 3, 1, 4, 1, 3, 4, 1, 2),
        ), cellClick = {}
    )
}
