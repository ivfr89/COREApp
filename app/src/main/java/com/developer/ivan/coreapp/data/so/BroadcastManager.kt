package com.developer.ivan.coreapp.data.so

import android.app.Application
import android.app.DownloadManager
import android.content.Context.DOWNLOAD_SERVICE
import android.database.Cursor
import android.net.Uri
import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.data.datasources.BroadcastDataSource.DownloadState
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform

class BroadcastManager(private val application: Application) : BroadcastDataSource {

    private val downloadManager = application.getSystemService(DOWNLOAD_SERVICE) as DownloadManager


    override fun downloadFile(
        title: String,
        id: String,
        url: String
    ): Flow<Either<Failure, DownloadState>> {


        val subpath = "${title.replace(" ", "_").replace(".", "")}_$id.pdf"
        val path = "/CorePDF"

        return try {
            val downloadRequest = DownloadManager.Request(Uri.parse(url)).apply {
                setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                setTitle(title)
                setDestinationInExternalFilesDir(application, path, subpath)

            }

            val downloadId = downloadManager.enqueue(downloadRequest)


            flow {
                emit(Either.Right(DownloadState.Init))
            }.transform {
                var bytesDownloaded = 0
                var bytesTotal = 1

                emit(Either.Right(DownloadState.Progress(0)))

                while (bytesDownloaded < bytesTotal) {
                    kotlinx.coroutines.delay(500)

                    val query = DownloadManager.Query()
                    query.setFilterById(downloadId)
                    val cursor: Cursor = downloadManager.query(query)


                    cursor.moveToFirst()

                    bytesDownloaded =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))

                    bytesTotal =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

                    emit(Either.Right(DownloadState.Progress((bytesDownloaded / bytesTotal) * 100)))
                }

                val downloadURI = application.getExternalFilesDir(path)

                emit(Either.Right(DownloadState.Completed(Uri.withAppendedPath(Uri.fromFile(downloadURI), subpath).toString())))
            }
        } catch (exception: IllegalArgumentException) {

            flow { emit(Either.Right(DownloadState.Cancel)) }
        }


    }
}