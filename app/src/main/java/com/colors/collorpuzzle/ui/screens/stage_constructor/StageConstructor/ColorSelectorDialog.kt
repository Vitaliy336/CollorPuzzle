package com.colors.collorpuzzle.ui.screens.stage_constructor.StageConstructor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.ui.screens.CellType

@Composable
fun ColorToFillPalettePicker(
    previousSelectedColor: Int,
    dismissRequest: () -> Unit,
    confirmRequest: (color: Int) -> Unit,
) {
    Dialog(onDismissRequest = dismissRequest) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .height(320.dp)
                .padding(all = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            DialogUi(
                modifier = Modifier,
                previousSelectedColor = previousSelectedColor,
                dismissRequest = dismissRequest,
                confirmRequest = confirmRequest
            )
        }
    }
}


@Composable
private fun DialogUi(
    modifier: Modifier = Modifier,
    previousSelectedColor: Int,
    confirmRequest: (Int) -> Unit,
    dismissRequest: () -> Unit,
) {
    val selectedColor = remember { mutableIntStateOf(previousSelectedColor) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, end = 16.dp, start = 16.dp, bottom = 8.dp)

    ) {
        DialogTitle(
            modifier = modifier
                .align(alignment = Alignment.TopCenter)
        )
        PickerBox(
            modifier = modifier
                .align(alignment = Alignment.Center),
            selectedColor = selectedColor.intValue,
            updateSelectedColorClick = { newColor ->
                selectedColor.intValue = newColor
            }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
            modifier = modifier
                .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 20.dp)
                .align(alignment = Alignment.BottomCenter)
        ) {
            CancelButton(buttonClick = {
                dismissRequest.invoke()
            })
            ConfirmButton(buttonClick = {
                confirmRequest(selectedColor.intValue)
            })
        }
    }
}

@Composable
private fun DialogTitle(modifier: Modifier) {
    Text(
        text = stringResource(R.string.dialog_color_picker_title),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun PickerBox(
    modifier: Modifier,
    selectedColor: Int,
    updateSelectedColorClick: (Int) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        CellType.getColorPairs().forEach { pair ->
            ColorPairRow(
                modifier = modifier,
                colorPair = pair,
                selectedColor = selectedColor,
                updateSelectedColorClick = updateSelectedColorClick
            )
        }
    }
}

@Composable
private fun ColorPairRow(
    modifier: Modifier,
    selectedColor: Int,
    colorPair: Pair<CellType, CellType>,
    updateSelectedColorClick: (Int) -> Unit,
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .padding(all = 2.dp)
    ) {
        ColorBox(
            modifier = modifier
                .padding(horizontal = 2.dp),
            cellType = colorPair.first,
            selectedColor = selectedColor,
            updateSelectedColorClick = updateSelectedColorClick
        )
        ColorBox(
            modifier = modifier
                .padding(horizontal = 2.dp),
            cellType = colorPair.second,
            selectedColor = selectedColor,
            updateSelectedColorClick = updateSelectedColorClick
        )
    }
}

@Composable
private fun ColorBox(
    modifier: Modifier,
    cellType: CellType,
    selectedColor: Int,
    updateSelectedColorClick: (Int) -> Unit,
) {
    val borderColor =
        if (CellType.getCellColor(selectedColor) == cellType) Color.Black else Color.LightGray
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(shape = RoundedCornerShape(20))
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(20)
            )
            .clickable(onClick = {
                updateSelectedColorClick(cellType.color)
            })
            .background(color = cellType.colorValue)
    )
}

@Composable
private fun ConfirmButton(buttonClick: () -> Unit) {
    Button(onClick = {
        buttonClick.invoke()
    }) {
        Text(stringResource(R.string.dialog_color_picker_confirm))
    }
}

@Composable
private fun CancelButton(buttonClick: () -> Unit) {
    OutlinedButton(onClick = {
        buttonClick.invoke()
    }) {
        Text(stringResource(R.string.dialog_color_picker_cancel))
    }
}

@Preview
@Composable
private fun DialogPreview() {
    ColorToFillPalettePicker(
        confirmRequest = { 1 }, dismissRequest = {},
        previousSelectedColor = 1
    )
}

@Preview
@Composable
private fun DialogPreviewUnselected() {
    ColorToFillPalettePicker(
        confirmRequest = { 1 }, dismissRequest = {},
        previousSelectedColor = 0
    )
}