package com.colors.collorpuzzle.ui.shared.stage_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_4_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.ui.screens.CellType


@Composable
fun PuzzleDialog(
    dismissRequest: () -> Unit,
    confirmRequest: () -> Unit,
    isSingleButtonDialog: Boolean = true,
    dialogTitle: String,
    confirmBtnText: String = "",
    dismissBtnText: String = "",
) {
    Dialog(
        onDismissRequest = { dismissRequest() },
    ) {
        Card(
            modifier = Modifier
                .wrapContentWidth()
                .height(240.dp)
                .padding(all = 16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            ShowPuzzleDialog(
                dialogText = dialogTitle,
                isSingleButtonDialog = isSingleButtonDialog,
                dismissClick = dismissRequest,
                confirmClick = confirmRequest,
                dismissBtnText = dismissBtnText,
                confirmBtnText = confirmBtnText
            )
        }
    }
}

@Composable
fun ShowPuzzleDialog(
    modifier: Modifier = Modifier,
    dialogText: String,
    isSingleButtonDialog: Boolean,
    confirmClick: () -> Unit,
    dismissClick: () -> Unit,
    confirmBtnText: String,
    dismissBtnText: String,
) {
    Box(
        modifier.fillMaxSize()
    ) {
        TitleSection(modifier.align(alignment = Alignment.TopCenter))
        MessageSection(
            modifier = modifier
                .align(alignment = Alignment.Center)
                .padding(bottom = 20.dp, end = 16.dp, start = 16.dp),
            dialogText = dialogText
        )
        ActionButtons(
            modifier = modifier.align(alignment = Alignment.BottomCenter),
            confirmClick = confirmClick,
            dismissClick = dismissClick,
            isSingleButtonDialog = isSingleButtonDialog,
            confirmBtnText = confirmBtnText,
            dismissBtnText = dismissBtnText
        )
    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            CellType.getPaletteColors().forEach {
                PaletteItem(color = it)
            }
        }
    }
}

@Composable
fun PaletteItem(
    modifier: Modifier = Modifier,
    color: CellType,
) {
    Box(
        modifier = modifier
            .padding(all = 2.dp)
            .clip(shape = RoundedCornerShape(20f))
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(20f))
            .size(24.dp)
            .background(color = color.colorValue)
    )
}

@Composable
fun MessageSection(modifier: Modifier = Modifier, dialogText: String) {
    Text(
        text = dialogText,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineLarge
    )
}

@Composable
fun ActionButtons(
    modifier: Modifier = Modifier,
    isSingleButtonDialog: Boolean,
    confirmClick: () -> Unit,
    dismissClick: () -> Unit,
    confirmBtnText: String,
    dismissBtnText: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
        modifier = modifier
            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        if (!isSingleButtonDialog) {
            CancelButton(dismissBtnText, dismissClick)
        }
        ConfirmButton(confirmBtnText, confirmClick)
    }
}

@Composable
fun ConfirmButton(btnText: String, buttonClick: () -> Unit) {
    Button(onClick = {
        buttonClick()
    }) {
        Text(btnText)
    }
}

@Composable
fun CancelButton(btnText: String, buttonClick: () -> Unit) {
    OutlinedButton(onClick = {
        buttonClick()
    }) {
        Text(btnText)
    }
}


@Composable
@Preview(device = PIXEL_4_XL)
fun DialogPreviewSingleButton() {
    PuzzleDialog(
        dialogTitle = stringResource(R.string.dialog_stage_cleared_msg),
        dismissRequest = {},
        confirmRequest = {},
        confirmBtnText = stringResource(R.string.dialog_btn_to_menu)
    )
}

@Composable
@Preview(device = PIXEL_4_XL)
fun DialogPreviewTwoButtons() {
    PuzzleDialog(
        dialogTitle = stringResource(R.string.dialog_stage_failed_msg),
        dismissRequest = {},
        confirmRequest = {},
        isSingleButtonDialog = false,
        confirmBtnText = stringResource(R.string.dialog_btn_to_menu),
        dismissBtnText = stringResource(R.string.dialog_btn_restart)
    )
}