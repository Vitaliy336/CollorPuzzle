package com.colors.collorpuzzle.ui.screens.stage_screen.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.R

@Composable
fun MovesLeftComponent(attempts: Int, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.attempts_counter),
            modifier = modifier.padding(horizontal = 0.dp, vertical = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = attempts.toString(), modifier = modifier
                .padding(top = 0.dp, bottom = 4.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
@Preview
fun MovesLeftPreview() {
    Surface {
        MovesLeftComponent(3)
    }
}