package com.developer.ivan.usecases

import com.developer.ivan.data.datasources.BroadcastDataSource.DownloadState
import com.developer.ivan.data.repositories.FileRepository
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DownloadFile(private val fileRepository: FileRepository) :
    UseCase<DownloadFile.Params, DownloadState>() {
    override suspend fun execute(params: Params): Flow<Either<Failure, DownloadState>> =
        fileRepository.downLoadFile(params.title,params.id, params.url )

    class Params(val title: String, val id: String, val url: String)

}