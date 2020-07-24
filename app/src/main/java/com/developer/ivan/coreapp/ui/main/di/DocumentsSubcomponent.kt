package com.developer.ivan.coreapp.ui.main.di

import com.developer.ivan.coreapp.di.ActivityScope
import com.developer.ivan.coreapp.ui.main.fragments.DocumentDetailFragment
import com.developer.ivan.coreapp.ui.main.fragments.DocumentListFragment
import dagger.Subcomponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
@ActivityScope
@Subcomponent(modules = [DocumentsModule::class])
interface DocumentsSubComponent {

//    val viewModel : MainViewModel

    fun inject(fragment: DocumentDetailFragment)
    fun inject(fragment: DocumentListFragment)


    @Subcomponent.Factory
    interface Factory {
        fun create(): DocumentsSubComponent
    }


}