package com.developer.ivan.coreapp.data.server.datasources

import com.developer.ivan.coreapp.data.server.ApiService
import com.developer.ivan.coreapp.data.server.NetworkManager
import com.developer.ivan.coreapp.data.server.mapper.JsonMapper
import com.developer.ivan.coreapp.data.server.mapper.ServerMapper
import com.developer.ivan.data.datasources.RemoteDataSource
import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@ExperimentalCoroutinesApi
class RetrofitDataSource(
    private val retrofit: ApiService,
    private val serverMapper: ServerMapper,
    private val jsonMapper: JsonMapper,
    private val headers: Map<String, String>
) : RemoteDataSource,
    NetworkManager by NetworkManager.NetworkImplementation() {

    private val SPANISH_QUERY = "Spanish"

    private fun buildQueryArticle(query: String) = "title:$query"

    override suspend fun searchArticles(
        query: String,
        page: Int,
        size: Int
    ): Either<Failure, List<Article>> =

        safeRequest(
            retrofit.searchArticles(
                query = buildQueryArticle(query),
                page = page,
                pageSize = size,
                headerMap = headers
            )
        ) { listArticles ->
            jsonMapper.getArray(listArticles.b) { jsonArray ->
                convertJsonToArticles(jsonArray) {
                    serverMapper.convertEntityArticleToDomain(
                        it
                    )
                }
            }
        }


    override suspend fun getArticle(id: Int): Either<Failure, Article> =
        safeRequest(retrofit.getArticle(id = id, headerMap = headers)) { article ->
            jsonMapper.getObject(article.b) { jsonObject ->
                convertJsonToArticle(jsonObject) {
                    serverMapper.convertEntityArticleToDomain(
                        it
                    )
                }
            }
        }
}

