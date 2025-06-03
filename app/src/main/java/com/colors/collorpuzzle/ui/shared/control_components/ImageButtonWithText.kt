package com.colors.collorpuzzle.ui.shared.control_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R

@Composable
fun ImageButtonWithTextComposable(
    modifier: Modifier = Modifier,
    componentText: String,
    painter: Painter,
    buttonClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        Image(
            painter = painter,
            contentDescription = "restart",
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .size(48.dp)
                .clip(shape = CircleShape)
                .clickable {
                    buttonClick()
                }
        )
        Text(text = componentText, textAlign = TextAlign.Center)
    }
}


@Composable
@Preview
fun RestartPreview() {
    Surface {
        ImageButtonWithTextComposable(
            buttonClick = {},
            componentText = stringResource(R.string.restart_btn_text),
            painter = painterResource(R.drawable.ic_restart)
        )
    }
}