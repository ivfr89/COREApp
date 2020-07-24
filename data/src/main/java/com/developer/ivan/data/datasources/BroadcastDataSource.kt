package com.developer.ivan.data.datasources

import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.flow.Flow

interface BroadcastDataSource {

    sealed class DownloadState {
        object Init : DownloadState()
        class Completed(val uri: String) : DownloadState()
        object Cancel : DownloadState()
        class Progress(val progress: Int) : DownloadState()

    }


    fun downloadFile(title: String, id: String, url: String) : Flow<Either<Failure, DownloadState>>
}