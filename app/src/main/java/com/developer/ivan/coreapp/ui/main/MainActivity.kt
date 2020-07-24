package com.developer.ivan.coreapp.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.room.Room
import com.developer.ivan.coreapp.R
import com.developer.ivan.coreapp.core.App
import com.developer.ivan.coreapp.data.db.DB
import com.developer.ivan.coreapp.data.db.datasources.RoomDataSource
import com.developer.ivan.coreapp.data.db.mapper.DBMapper
import com.developer.ivan.coreapp.data.server.datasources.RetrofitDataSource
import com.developer.ivan.coreapp.data.so.BroadcastManager
import com.developer.ivan.coreapp.ui.checkers.PermissionCheckerImplementation
import com.developer.ivan.coreapp.ui.checkers.PermissionRequesterImplementation
import com.developer.ivan.coreapp.ui.main.di.DocumentsSubComponent
import com.developer.ivan.coreapp.ui.mapper.UIMapper
import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.data.repositories.ArticleRepository
import com.developer.ivan.data.repositories.ArticleRepositoryImplementation
import com.developer.ivan.data.repositories.FileRepositoryImplementation
import com.developer.ivan.usecases.DownloadFile
import com.developer.ivan.usecases.GetArticle
import com.developer.ivan.usecases.SearchArticles
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var nav: NavController

    lateinit var documentsSubComponent: DocumentsSubComponent

//    @Inject
//    lateinit var mViewModelFactory: ViewModelFactory

//    lateinit var mViewModel: MainViewModel


//    @Inject
//    lateinit var retrofitDataSource: RemoteDataSource
//
//    @Inject
//    lateinit var localDataSource: LocalDataSource

    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as App).getAppComponent().inject(this)

        documentsSubComponent = (application as App).getAppComponent().documentsSubcomponent
            .create()

//        mViewModel = ViewModelProvider(this, mViewModelFactory)[MainViewModel::class.java]

        /*mViewModel = ViewModelProvider(
            this, MainViewModel.Factory(
                GetArticle(
                    ArticleRepositoryImplementation(localDataSource,remoteDataSource = retrofitDataSource)
                ),
                SearchArticles(ArticleRepositoryImplementation(localDataSource,remoteDataSource = retrofitDataSource)),
                DownloadFile(
                    FileRepositoryImplementation(
                        BroadcastManager(application),
                        PermissionRequesterImplementation(application),
                        PermissionCheckerImplementation(application)
                    )
                ),
                UIMapper()
            ))[MainViewModel::class.java]*/

        setContentView(R.layout.activity_main)

        nav = findNavController(R.id.navigation)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    override fun onSupportNavigateUp(): Boolean {
        return (nav.navigateUp()
                || super.onSupportNavigateUp())
    }


}
