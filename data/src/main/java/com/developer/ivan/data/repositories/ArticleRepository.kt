package com.developer.ivan.data.repositories

import com.developer.ivan.data.datasources.BroadcastDataSource
import com.developer.ivan.data.datasources.BroadcastDataSource.DownloadState
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import com.developer.ivan.domain.flatMapToRight
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*

interface ArticleRepository {

    suspend fun searchArticles(
        query: String,
        page: Int,
        size: Int,
        force: Boolean = true
    ): Flow<Either<Failure, List<Article>>>

    suspend fun getArticle(
        id: Int,
        force: Boolean = true
    ): Flow<Either<Failure, Article>>



    suspend fun getCount(): Flow<Either<Failure, Int>>

}

@FlowPreview
class ArticleRepositoryImplementation(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : ArticleRepository {
    override suspend fun searchArticles(
        query: String,
        page: Int,
        size: Int,
        force: Boolean
    ): Flow<Either<Failure, List<Article>>> {

        return when (force) {
            true -> {

                flow {
                    val result = remoteDataSource.searchArticles(query, page, size)


                    emit(result.flatMapToRight {
                        localDataSource.insertArticlesWithQuery(query, it)
                        Either.Right(it)
                    })

                }

            }
            false -> {

                localDataSource.searchArticles(query, page, size).distinctUntilChanged()
                    .flatMapLatest { result ->


                       when (result) {
                            is Either.Left -> doNetworkSearchArticles(query, page, size)
                            is Either.Right -> if (result.b.isNotEmpty()) {
                                flow {
                                    emit(result)
                                }
                            } else {
                                doNetworkSearchArticles(query, page, size)
                            }
                        }


                    }
            }
        }
    }

    private fun doNetworkSearchArticles(query: String,
                                page: Int,
                                size: Int) : Flow<Either<Failure,List<Article>>> = flow {
        val network = remoteDataSource.searchArticles(query, page, size)

        emit(network.flatMapToRight {
            localDataSource.insertArticlesWithQuery(query, it)
            Either.Right(it)
        })
    }

    override suspend fun getArticle(id: Int, force: Boolean): Flow<Either<Failure, Article>> {

        return when (force) {
            true -> {

                return flow {
                    val result = remoteDataSource.getArticle(id)

                    emit(result.flatMapToRight {
                        localDataSource.updateArticle(it)
                        Either.Right(it)
                    })
                }
            }
            false -> localDataSource.getArticle(id)
        }

    }
    override suspend fun getCount(): Flow<Either<Failure, Int>> = localDataSource.totalAmount()

}