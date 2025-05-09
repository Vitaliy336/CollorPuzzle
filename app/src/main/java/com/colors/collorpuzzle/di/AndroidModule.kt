package com.colors.collorpuzzle.di

import com.colors.collorpuzzle.ui.screens.main_menu.view_model.MainMenuViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MainMenuViewModel)
}