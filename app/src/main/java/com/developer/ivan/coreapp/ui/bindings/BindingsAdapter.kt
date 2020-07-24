package com.developer.ivan.coreapp.ui.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.developer.ivan.coreapp.ui.adapters.ArticleListAdapter
import com.developer.ivan.coreapp.ui.main.MainViewModel

@BindingAdapter("data")
fun setAdapterData(recyclerView: RecyclerView?, data: MainViewModel.ArticlesListState?){

    val adapter = recyclerView?.adapter
    if(adapter is ArticleListAdapter){
        when(data){
            MainViewModel.ArticlesListState.Idle -> adapter.submitList(emptyList())
            is MainViewModel.ArticlesListState.ShowItems -> adapter.submitList(data.documentList)
            else -> {}
        }
    }

}