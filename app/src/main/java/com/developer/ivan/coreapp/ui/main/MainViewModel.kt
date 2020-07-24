@file:Suppress("UNCHECKED_CAST")

package com.developer.ivan.coreapp.ui.main

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.developer.ivan.coreapp.core.Event
import com.developer.ivan.coreapp.di.ActivityScope
import com.developer.ivan.coreapp.ui.mapper.UIMapper
import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.domain.Constants
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.usecases.DownloadFile
import com.developer.ivan.usecases.GetArticle
import com.developer.ivan.usecases.SearchArticles
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@ActivityScope
class MainViewModel @Inject constructor(
    private val getArticle: GetArticle,
    private val searchArticles: SearchArticles,
    private val downloadFile: DownloadFile,
    private val uiMapper: UIMapper
) : ViewModel() {

    /*class Factory(
        private val getArticle: GetArticle,
        private val searchArticles: SearchArticles,
        private val downloadFile: DownloadFile,
        private val uiMapper: UIMapper
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MainViewModel(getArticle,searchArticles,downloadFile,uiMapper) as T
        }

    }*/

    sealed class ArticlesListState {
        object Idle : ArticlesListState()
        object NoResults : ArticlesListState()
        class Error(val failure: Failure) : ArticlesListState()
        class ShowItems(val documentList: List<UIArticle>) : ArticlesListState()
    }

    sealed class NavigationEvent {
        class ToDetail(val arg: UIArticle, val title: TextView) : NavigationEvent()
        class ToPDFView(val uri: String) : NavigationEvent()
    }

    private var _documentsStateListData = MutableLiveData<ArticlesListState>(ArticlesListState.Idle)

    val documentsStateListData: LiveData<ArticlesListState>
        get() = _documentsStateListData

    private val _articleDetail = MutableLiveData<UIArticle>()

    val articleDetail: LiveData<UIArticle>
        get() = _articleDetail

    private val _error = MutableLiveData<Failure>()
    val error: LiveData<Failure>
        get() = _error


    private val _progressBar = MutableLiveData(false)

    val progressBar: LiveData<Boolean>
        get() = _progressBar

    private val _progressDownload = MutableLiveData<Int>(-1)
    val progressDownload: LiveData<Int>
        get() = _progressDownload

//    Events

    private val _navigation = MutableLiveData<Event<NavigationEvent>>()
    val navigation: LiveData<Event<NavigationEvent>>
        get() = _navigation


    fun navigateTo(navEvent: NavigationEvent) {

        _navigation.value = Event(navEvent)

    }

    fun download() {


        viewModelScope.launch {

            val article = _articleDetail.value

            if (article?.downloadUrl != null) {
                downloadFile(
                    DownloadFile.Params(
                        article.title,
                        article.id.toString(),
                        article.downloadUrl
                    )
                )
                    .onStart {
                        _progressBar.value = true
                    }
                    .collect { result ->

                        when (result) {
                            is Either.Left -> {

                            }
                            is Either.Right -> {

                                when (val value = result.b) {
                                    BroadcastDataSource.DownloadState.Init -> Log.i(
                                        "Init",
                                        value.toString()
                                    )

                                    is BroadcastDataSource.DownloadState.Completed -> {
                                        Log.i(
                                            "Completed",
                                            value.uri
                                        )
                                        _navigation.value =
                                            Event(NavigationEvent.ToPDFView(value.uri))
                                        _progressDownload.value = -1
                                    }
                                    BroadcastDataSource.DownloadState.Cancel -> Log.i(
                                        "Cancel",
                                        value.toString()
                                    )
                                    is BroadcastDataSource.DownloadState.Progress -> {

                                        _progressDownload.value = value.progress
                                        Log.i(
                                            "Progress",

                                            value.progress.toString()
                                        )
                                    }
                                }
                            }
                        }
                    }
            }

        }
    }

    fun getArticle(id: Int) {
        viewModelScope.launch {
            getArticle(GetArticle.Params(id, false))
                .onStart {
                    _progressBar.value = true
                }
                .mapLatest { state ->
                    when (state) {
                        is Either.Right -> {
                            _articleDetail.value = uiMapper.convertArticleToUI(state.b)
                        }
                        is Either.Left -> _error.value = state.a
                    }
                    _progressBar.value = false

                }.collect {
                    Log.i("FLOW", "Value receive")
                }

        }
    }

    fun searchArticles(query: String, force: Boolean = true) {


        viewModelScope.launch {
            searchArticles(
                SearchArticles.Params(
                    query,
                    Constants.Server.DEFAULT_PAGE,
                    Constants.Server.DEFAULT_SIZE,
                    force
                )
            )
                .onStart {
                    _progressBar.value = true
                }
                .mapLatest { value ->
                    Log.i("FLOW", "Coming value")

                    val state = when (value) {
                        is Either.Right -> {
                            if (value.b.isEmpty())
                                ArticlesListState.NoResults
                            else
                                ArticlesListState.ShowItems(uiMapper.convertArticlesToUI(value.b))

                        }
                        is Either.Left -> ArticlesListState.Error(value.a)
                    }

                    _documentsStateListData.value = state
                    state

                }
                .catch { e ->
                    Log.e("FLOW", e.localizedMessage ?: "")

                }
                .collect {

                    if (it is ArticlesListState.ShowItems)
                        Log.i("FLOW", "Value receive ${it.documentList}")
                    _progressBar.value = false
                }

        }

    }

}