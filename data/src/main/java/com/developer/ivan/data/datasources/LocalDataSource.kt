package com.developer.ivan.data.datasources

import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

     fun searchArticles(query: String, page: Int, size: Int): Flow<Either<Failure, List<Article>>>
     fun getArticle(id: Int): Flow<Either<Failure, Article>>
     fun totalAmount(): Flow<Either<Failure, Int>>

     suspend fun insertArticlesWithQuery(query: String, articles: List<Article>)
     suspend fun updateArticle(article: Article)
}