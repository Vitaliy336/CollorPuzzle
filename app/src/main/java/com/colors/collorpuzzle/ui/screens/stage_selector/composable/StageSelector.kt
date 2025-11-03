package com.colors.collorpuzzle.ui.screens.stage_selector.composable

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colors.collorpuzzle.R
import com.colors.collorpuzzle.ui.shared.loader.ShowLoader
import com.colors.collorpuzzle.ui.screens.stage_selector.view_model.StageSelectionIntent
import com.colors.collorpuzzle.ui.screens.stage_selector.view_model.StageSelectorViewModel
import com.colors.collorpuzzle.ui.screens.stage_selector.view_model.StagesData
import com.colors.collorpuzzle.ui.theme.AppTheme
import org.koin.compose.viewmodel.koinViewModel

private const val TAG = "StageSelector"

@Composable
fun StageSelectorScreen(
    modifier: Modifier,
    backClick: () -> Unit,
    selectStageClick: (stageName: String) -> Unit,
) {
    val vm = koinViewModel<StageSelectorViewModel>()
    val state = vm.levelsStateFlow.collectAsState()

    LaunchedEffect(Unit) {
        vm.handleIntent(StageSelectionIntent.FetchStages)
    }

    when (state.value) {
        is StageSelectorViewModel.LevelsState.Loading -> {
            ShowLoader(isFinished = {})
        }

        is StageSelectorViewModel.LevelsState.Error -> {
            Log.d(TAG, "StageSelectorScreen: ")
        }

        is StageSelectorViewModel.LevelsState.Success -> {
            ShowStages(
                stagesData = (state.value as StageSelectorViewModel.LevelsState.Success).data,
                modifier = modifier,
                backClick = backClick,
                selectStageClick = selectStageClick
            )
        }
    }
}

@Composable
private fun ShowStages(
    stagesData: List<StagesData>,
    modifier: Modifier = Modifier,
    backClick: () -> Unit,
    selectStageClick: (stageName: String) -> Unit,
) {
    AppTheme {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 20.dp,
                    bottom = 20.dp
                )
        ) {
            BackButton(
                modifier = Modifier.align(alignment = Alignment.End),
                backClick = backClick
            )

            Text(
                text = stringResource(R.string.stage_select), modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        top = 12.dp,
                        bottom = 12.dp
                    ),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )


            StagesList(
                stagesList = stagesData,
                stageClick = selectStageClick
            )
        }
    }

}

@Composable
private fun BackButton(
    backClick: () -> Unit,
    modifier: Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.close),
        contentDescription = "back",
        contentScale = ContentScale.FillBounds,
        modifier = modifier
            .size(32.dp)
            .clickable {
                backClick()
            }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StagesList(
    stagesList: List<StagesData>,
    stageClick: (stageName: String) -> Unit,
) {
    LazyColumn {
        items(stagesList) { stage ->
            StagesRow(
                stageItems = stage.stagesList,
                stageHeader = stage.stagesGroupName,
                stageClick = stageClick
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StagesRow(
    modifier: Modifier = Modifier,
    stageHeader: String,
    stageItems: List<StagesData.StageData> = listOf(),
    stageClick: (stageName: String) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .wrapContentHeight()
    ) {
        CategoryHeader(text = stageHeader, modifier = modifier)
        FlowRow(
            modifier = modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            maxItemsInEachRow = 5
        ) {
            repeat(stageItems.size) { index ->
                StageItem(
                    stageName = stageItems[index].stageName,
                    stageIndex = index.toString(),
                    isCleared = stageItems[index].isCleared,
                    modifier = modifier.padding(vertical = 4.dp),
                    stageClick = stageClick
                )
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier.height(8.dp))
    Text(
        text = text,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
            .fillMaxWidth()
            .clip(CircleShape)
            .background(color = MaterialTheme.colorScheme.tertiaryFixed)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                shape = CircleShape
            ),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onTertiaryFixed
    )
    Spacer(modifier = modifier.height(8.dp))
}

@Composable
private fun StageItem(
    stageName: String,
    stageIndex: String,
    isCleared: Boolean,
    modifier: Modifier,
    stageClick: (stageName: String) -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(percent = 30))
            .size(64.dp)
            .background(
                if (isCleared) MaterialTheme.colorScheme.tertiary else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onSurface,
                shape = RoundedCornerShape(percent = 30)
            )
            .clickable {
                stageClick(stageName)
            }
    ) {
        Text(text = stageIndex, color = MaterialTheme.colorScheme.background)
    }
}

@Preview
@Composable
private fun StagesRowPreview() {
    StagesRow(
        stageItems = listOf(),
        stageHeader = "name1",
        stageClick = {}
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ShowStagesListPreviewNight() {
    ShowStages(
        stagesData = listOf<StagesData>(
            StagesData(
                "group 1",
                listOf(
                    StagesData.StageData("stage1", false),
                    StagesData.StageData("stage2", true),
                    StagesData.StageData("stage2", false),
                )
            ),
            StagesData(
                "group 2",
                listOf(
                    StagesData.StageData("stage1", true),
                    StagesData.StageData("stage2", true),
                    StagesData.StageData("stage2", false),
                )
            )
        ),
        modifier = Modifier,
        backClick = {},
        selectStageClick = {},
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ShowStagesListPreview() {
    ShowStages(
        stagesData = listOf<StagesData>(
            StagesData(
                "group 1",
                listOf(
                    StagesData.StageData("stage1", false),
                    StagesData.StageData("stage2", true),
                    StagesData.StageData("stage2", false),
                )
            ),
            StagesData(
                "group 2",
                listOf(
                    StagesData.StageData("stage1", true),
                    StagesData.StageData("stage2", true),
                    StagesData.StageData("stage2", false),
                )
            )
        ),
        modifier = Modifier,
        backClick = {},
        selectStageClick = {},
    )
}

@Preview
@Composable
private fun StageItemPreview() {
    StageItem(
        modifier = Modifier,
        stageName = "some name",
        stageIndex = "1",
        isCleared = false,
        stageClick = {})
}

@Preview
@Composable
private fun StageItemPreviewCleared() {
    StageItem(
        modifier = Modifier,
        stageName = "some name 2",
        stageIndex = "2",
        isCleared = true,
        stageClick = {})
}
