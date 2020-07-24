package com.developer.ivan.coreapp.ui.main.di

import androidx.lifecycle.ViewModel
import com.developer.ivan.coreapp.di.ActivityScope
import com.developer.ivan.coreapp.di.ViewModelKey
import com.developer.ivan.coreapp.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class DocumentsModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideViewModel(viewModel: MainViewModel): ViewModel

}