package com.colors.collorpuzzle.ui.screens.main_menu.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.colors.collorpuzzle.ui.screens.main_menu.view_model.MainMenuViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ShowMainMenu(
    modifier: Modifier,
    playClicked: () -> Unit,
    randomStageClicked: () -> Unit,
    constructorClicked: () -> Unit,
) {

    val vm = koinViewModel<MainMenuViewModel>()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(text = "Main menu")
        val btnModifier: Modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(start = 200.dp, end = 200.dp, top = 8.dp, bottom = 8.dp)
        MenuButton(modifier = btnModifier, "Play", playClicked)
        MenuButton(modifier = btnModifier, "Random Mode", randomStageClicked)
        MenuButton(modifier = btnModifier, "Constructor", constructorClicked)
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
fun ShowMainMenuPreview() {
    ShowMainMenu(
        modifier = Modifier.fillMaxSize(),
        playClicked = {},
        randomStageClicked = {},
        constructorClicked = {})
}