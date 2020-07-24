package com.developer.ivan.coreapp.di

import com.developer.ivan.coreapp.data.db.datasources.RoomDataSource
import com.developer.ivan.coreapp.data.db.mapper.DBMapper
import com.developer.ivan.coreapp.data.server.ApiService
import com.developer.ivan.coreapp.data.server.datasources.RetrofitDataSource
import com.developer.ivan.coreapp.data.server.mapper.ServerMapper
import com.developer.ivan.coreapp.ui.mapper.UIMapper
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.data.repositories.ArticleRepository
import com.developer.ivan.data.repositories.ArticleRepositoryImplementation
import com.developer.ivan.data.repositories.FileRepository
import com.developer.ivan.domain.Constants
import com.developer.ivan.usecases.DownloadFile
import com.developer.ivan.usecases.GetArticle
import com.developer.ivan.usecases.SearchArticles
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class UseCasesModule {


    @Singleton
    @Provides
    fun provideGetArticle(articleRepository: ArticleRepository) = GetArticle(articleRepository)

    @Singleton
    @Provides
    fun provideDownloadFile(fileRepository: FileRepository) = DownloadFile(fileRepository)



    @Singleton
    @Provides
    fun provideSearchArticles(articleRepository: ArticleRepository) = SearchArticles(articleRepository)

}