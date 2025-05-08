package com.colors.collorpuzzle.ui.screens.main_menu.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.ui.screens.main_menu.view_model.MainMenuViewModel
import org.koin.compose.viewmodel.koinViewModel
import com.colors.collorpuzzle.R

@Composable
fun ShowMainMenu(
    modifier: Modifier,
    playClicked: () -> Unit,
    randomStageClicked: () -> Unit,
    constructorClicked: () -> Unit,
) {

    val vm = koinViewModel<MainMenuViewModel>()

    Surface {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            Text(text = stringResource(R.string.main_menu),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface)
            val btnModifier: Modifier = Modifier
                .height(56.dp)
                .fillMaxWidth()
                .padding(start = 200.dp, end = 200.dp, top = 8.dp, bottom = 8.dp)
            MenuButton(
                modifier = btnModifier,
                stringResource(R.string.start_game),
                playClicked
            )
            MenuButton(
                modifier = btnModifier,
                stringResource(R.string.random_mode),
                randomStageClicked
            )
            MenuButton(
                modifier = btnModifier,
                stringResource(R.string.constructor_mode),
                constructorClicked
            )
        }
    }

    vm.test()
}

@Composable
fun MenuButton(
    modifier: Modifier,
    text: String,
    click: () -> Unit,
) {
    OutlinedButton(
        onClick = click,
        modifier = modifier
    ) {
        Text(text = text)
    }
}


@Preview
@Composable
fun ButtonPreview() {
    MenuButton(
        modifier = Modifier,
        "Some Text",
        {})
}

@Preview
@Composable
fun ShowMainMenuPreview() {
    ShowMainMenu(
        modifier = Modifier.fillMaxSize(),
        playClicked = {},
        randomStageClicked = {},
        constructorClicked = {})
}