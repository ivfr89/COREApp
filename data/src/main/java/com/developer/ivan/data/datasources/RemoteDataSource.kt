package com.developer.ivan.data.datasources

import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Constants.Server.DEFAULT_PAGE
import com.developer.ivan.domain.Constants.Server.DEFAULT_SIZE
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun searchArticles(query: String, page: Int, size: Int): Either<Failure, List<Article>>
    suspend fun getArticle(id: Int): Either<Failure, Article>

}