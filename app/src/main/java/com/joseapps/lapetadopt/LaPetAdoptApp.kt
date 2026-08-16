package com.joseapps.lapetadopt

import android.app.Application
import com.joseapps.lapetadopt.di.AppContainer

class LaPetAdoptApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
