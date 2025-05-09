package com.colors.collorpuzzle.di

import com.colors.collorpuzzle.data.repo.RemoteConfigRepo
import com.colors.collorpuzzle.data.repo.RemoteConfigRepoImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val configModule = module {
    singleOf(::RemoteConfigRepoImpl) bind RemoteConfigRepo::class
}