package com.colors.collorpuzzle.ui.screens.stage_constructor.stage_constructor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.colors.collorpuzzle.data.CellType
import com.colors.collorpuzzle.ui.shared.stage_dialog.PaletteItem

@Composable
fun ExportDialog(
    msg: String,
    btnText: String,
    dismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = dismissRequest) {
        Card(
            modifier = Modifier
                .padding(all = 16.dp)
                .height(240.dp)
                .wrapContentWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    CellType.getPaletteColors().forEach {
                        PaletteItem(color = it)
                    }
                }

                Text(
                    text = msg,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .align(Alignment.Center)
                        .padding(all = 8.dp),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
                Button(
                    onClick = { dismissRequest.invoke() },
                            modifier = Modifier . align (Alignment.BottomCenter)
                ) {
                    Text(
                        text = btnText,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun ExportDialogPreview() {
    ExportDialog(
        msg = "all palette cells should be filled",
        btnText = "some text",
        dismissRequest = {}
    )
}
