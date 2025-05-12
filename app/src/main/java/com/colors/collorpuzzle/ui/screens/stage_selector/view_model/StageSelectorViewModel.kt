package com.colors.collorpuzzle.ui.screens.stage_selector.view_model

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colors.collorpuzzle.data.local.PuzzleDataStore
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TAG = "StageSelectorViewModel"

class StageSelectorViewModel(
    val repo: RemoteConfigRepo,
    val localStorage: PuzzleDataStore,
) : ViewModel() {

    val levelsStateFlow = MutableStateFlow<LevelsState>(LevelsState.Loading)
    private val _levels = localStorage.clearedLevelsFlow
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

    sealed class LevelsState {
        object Loading : LevelsState()
        object Error : LevelsState()
        data class Success(val data: List<StagesData>) : LevelsState()
    }

    fun test() {
        Log.d(TAG, "test: ")
        viewModelScope.launch {
            _config.combine(_levels) { config, levels ->
                when (config) {
                    is RemoteConfigRepoImpl.ConfigState.Error -> levelsStateFlow.value =
                        LevelsState.Error

                    is RemoteConfigRepoImpl.ConfigState.Loading -> levelsStateFlow.value =
                        LevelsState.Loading

                    is RemoteConfigRepoImpl.ConfigState.Success -> levelsStateFlow.value =
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
