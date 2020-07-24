package com.developer.ivan.coreapp.core

import android.app.Application
import com.developer.ivan.coreapp.di.AppComponent
import com.developer.ivan.coreapp.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class App : Application() {

    lateinit var component: AppComponent
        private set

    override fun onCreate() {
        component = getAppComponent()
        super.onCreate()
    }


    open fun getAppComponent(): AppComponent =
        DaggerAppComponent
            .factory()
            .create(this)
}