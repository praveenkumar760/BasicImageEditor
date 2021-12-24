package com.praveen.basicimageeditor.module

import android.app.Application
import org.koin.core.context.startKoin

class ApplicationModule : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
           modules(viewModelModule) }
        }
    }