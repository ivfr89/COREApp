package com.developer.ivan.coreapp.di

import android.app.Application
import androidx.room.Room
import com.developer.ivan.coreapp.BuildConfig
import com.developer.ivan.coreapp.data.db.DB
import com.developer.ivan.coreapp.data.db.datasources.RoomDataSource
import com.developer.ivan.coreapp.data.db.mapper.DBMapper
import com.developer.ivan.coreapp.data.server.ApiService
import com.developer.ivan.coreapp.data.server.datasources.RetrofitDataSource
import com.developer.ivan.coreapp.data.server.mapper.JsonMapper
import com.developer.ivan.coreapp.data.server.mapper.ServerMapper
import com.developer.ivan.coreapp.data.so.BroadcastManager
import com.developer.ivan.coreapp.ui.checkers.PermissionCheckerImplementation
import com.developer.ivan.coreapp.ui.checkers.PermissionRequesterImplementation
import com.developer.ivan.coreapp.ui.mapper.UIMapper
import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.data.repositories.ArticleRepository
import com.developer.ivan.data.repositories.ArticleRepositoryImplementation
import com.developer.ivan.data.repositories.FileRepository
import com.developer.ivan.data.repositories.FileRepositoryImplementation
import com.developer.ivan.data.utils.PermissionChecker
import com.developer.ivan.data.utils.PermissionRequester
import com.developer.ivan.domain.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
class AppModule {


    @Singleton
    @Provides
    fun provideHttpClient() = OkHttpClient.Builder()
        .callTimeout(Constants.Server.OkHTTPConfig.timeOut, TimeUnit.MILLISECONDS)
        .connectTimeout(Constants.Server.OkHTTPConfig.timeOut, TimeUnit.MILLISECONDS)
        .build()


    @Singleton
    @Named("base_url")
    @Provides
    fun provideBaseUrl() = Constants.Server.BASE_URL

    @Singleton
    @Provides
    fun provideRetrofitClient(
        okHttpClient: OkHttpClient,
        @Named("base_url") baseUrl: String
    ) = Retrofit.Builder().client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(baseUrl)
        .build().create(ApiService::class.java)


    @Singleton
    @Provides
    fun provideArticleRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): ArticleRepository = ArticleRepositoryImplementation(localDataSource, remoteDataSource)

    @Singleton
    @Provides
    fun provideFileRepository(
        broadcastDataSource: BroadcastDataSource,
        permissionRequester: PermissionRequester,
        permissionChecker: PermissionChecker
    ): FileRepository =
        FileRepositoryImplementation(broadcastDataSource, permissionRequester, permissionChecker)


    @Singleton
    @Provides
    fun provideMoshi(
    ): Moshi = Moshi.Builder().build()


    @Singleton
    @Provides
    fun provideDbMapper() = DBMapper()

    @Singleton
    @Provides
    fun provideServerMapper() = ServerMapper()

    @Singleton
    @Provides
    fun provideJsonMapper(moshi: Moshi) = JsonMapper(moshi)


    @Singleton
    @Provides
    fun provideUIMapper() = UIMapper()


    @Singleton
    @Provides
    fun providePermissionRequester(
        application: Application
    ): PermissionRequester = PermissionRequesterImplementation(application)

    @Singleton
    @Provides
    fun providePermissionChecker(application: Application): PermissionChecker =
        PermissionCheckerImplementation(application)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        dbMapper: DBMapper,
        database: DB
    ): LocalDataSource = RoomDataSource(dbMapper, database)

    @Singleton
    @Provides
    fun provideBroadcastDataSource(application: Application): BroadcastDataSource =
        BroadcastManager(application)


    @Singleton
    @Provides
    fun provideRemoteDataSource(
        apiService: ApiService,
        serverMapper: ServerMapper,
        jsonMapper: JsonMapper
    ): RemoteDataSource = RetrofitDataSource(
        apiService,
        serverMapper,
        jsonMapper,
        mapOf(Constants.Server.Headers.API to BuildConfig.API_KEY)
    )

    @Singleton
    @Provides
    fun provideDatabase(
        application: Application
    ) = Room.databaseBuilder(application, DB::class.java, "db").fallbackToDestructiveMigration()
        .build()

}