package com.colors.collorpuzzle.di

import com.colors.collorpuzzle.data.local.IPuzzleDataStore
import com.colors.collorpuzzle.data.local.PuzzleDataStore
import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl
import com.google.gson.Gson
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val configModule = module {
    singleOf(::RemoteConfigRepoImpl) bind RemoteConfigRepo::class
    singleOf(::PuzzleDataStore) bind IPuzzleDataStore::class
    singleOf(::Gson) bind Gson::class
}