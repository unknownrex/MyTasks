package com.rexample.mytasks

import android.app.Application
import com.rexample.mytasks.data.di.dataModule
import com.rexample.mytasks.ui.di.appModule
import com.rexample.mytasks.ui.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule, dataModule, uiModule
            )
        }
    }
}