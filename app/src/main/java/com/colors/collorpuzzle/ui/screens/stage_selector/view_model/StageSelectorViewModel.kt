package com.colors.collorpuzzle.ui.screens.stage_selector.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.data.local.PuzzleDataStore
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl
import com.colors.collorpuzzle.ui.screens.stage_selector.StageSelectionIntent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StageSelectorViewModel(
    repo: RemoteConfigRepo,
    localStorage: PuzzleDataStore,
) : ViewModel() {

    private val _levelsFlow = MutableStateFlow<LevelsState>(LevelsState.Loading)
    private val _levels = localStorage.getClearedLvlFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = setOf()
        )
    private val _config = repo.getConfigState()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(1000),
            initialValue = RemoteConfigRepoImpl.ConfigState.Loading()
        )

    val levelsStateFlow: StateFlow<LevelsState> = _levelsFlow

    fun handleIntent(stageSelectionIntent: StageSelectionIntent) {
        when (stageSelectionIntent) {
            is StageSelectionIntent.FetchStages -> fetchLevelsData()
         }
    }

    sealed class LevelsState {
        object Loading : LevelsState()
        object Error : LevelsState()
        data class Success(val data: List<StagesData>) : LevelsState()
    }

   private fun fetchLevelsData() {
        viewModelScope.launch {
            _config.combine(_levels) { config, levels ->
                when (config) {
                    is RemoteConfigRepoImpl.ConfigState.Error -> _levelsFlow.value =
                        LevelsState.Error

                    is RemoteConfigRepoImpl.ConfigState.Loading -> {
                        _levelsFlow.value =
                            LevelsState.Loading
                        delay(2000) // just to simulate loading
                    }

                    is RemoteConfigRepoImpl.ConfigState.Success -> _levelsFlow.value =
                        LevelsState.Success(
                            data = config.config.map { stage ->
                                StagesData(
                                    stagesGroupName = stage.stageCategoryName,
                                    stagesList = stage.stages.map { stageItem ->
                                        StagesData.StageData(
                                            stageName = stageItem.stageName,
                                            isCleared = levels.contains(stageItem.stageName)
                                        )
                                    }
                                )
                            }
                        )
                }
            }.collect()
        }
    }
}
