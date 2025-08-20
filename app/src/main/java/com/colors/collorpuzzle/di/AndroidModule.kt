package com.colors.collorpuzzle.di

import com.colors.collorpuzzle.ui.screens.main_menu.view_model.MainMenuViewModel
import com.colors.collorpuzzle.ui.screens.stage_constructor.view_model.StageConstructorViewModel
import com.colors.collorpuzzle.ui.screens.stage_screen.stage_viewModel.StageViewModel
import com.colors.collorpuzzle.ui.screens.stage_selector.view_model.StageSelectorViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainMenuViewModel)
    viewModelOf(::StageSelectorViewModel)
    viewModelOf(::StageConstructorViewModel)
    viewModel { (stageName: String, stageJson: String) ->
        StageViewModel(repo = get(),
            localStorage = get(),
            gson = get(),
            stageName = stageName,
            stageData = stageJson)
    }
}