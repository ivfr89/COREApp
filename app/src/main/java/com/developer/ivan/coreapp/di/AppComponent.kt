package com.developer.ivan.coreapp.di

import android.app.Application
import android.content.Context
import com.developer.ivan.coreapp.ui.main.MainActivity
import com.developer.ivan.coreapp.ui.main.di.DocumentsSubComponent
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(modules = [AppSubcomponents::class, AppModule::class, UseCasesModule::class, ViewModelFactoryModule::class])
interface AppComponent {


    val documentsSubcomponent: DocumentsSubComponent.Factory


    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance applicationContext: Application): AppComponent
    }
}