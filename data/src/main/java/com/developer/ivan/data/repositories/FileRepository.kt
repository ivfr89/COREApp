package com.developer.ivan.data.repositories

import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.data.utils.PermissionChecker
import com.developer.ivan.data.utils.PermissionRequester
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

interface FileRepository {
    suspend fun downLoadFile(
        title: String,
        id: String,
        url: String
    ): Flow<Either<Failure, BroadcastDataSource.DownloadState>>

}

class FileRepositoryImplementation(
    private val broadcastDataSource: BroadcastDataSource,
    private val permissionRequester: PermissionRequester,
    private val permissionChecker: PermissionChecker
) :
    FileRepository {


    override suspend fun downLoadFile(
        title: String,
        id: String,
        url: String
    ): Flow<Either<Failure, BroadcastDataSource.DownloadState>> {
        return if (!permissionChecker.checkIfWriteIsGranted()) {

            flow {
                emit(permissionRequester.requestPermission(PermissionRequester.PERMISSION.WRITE))
            }.flatMapLatest {
                when (it) {
                    is Either.Right -> broadcastDataSource.downloadFile(title,id, url)
                    is Either.Left -> flow { emit(it as Either.Left) }
                }
            }
        } else
            broadcastDataSource.downloadFile(title, id, url)
    }

}