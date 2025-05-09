package com.colors.collorpuzzle

import android.app.Application
import com.colors.collorpuzzle.di.appModule
import com.colors.collorpuzzle.di.configModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PuzzleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@PuzzleApplication)
            modules(configModule, appModule)
        }
    }
}