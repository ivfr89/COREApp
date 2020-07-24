package com.developer.ivan.usecases

import com.developer.ivan.data.repositories.ArticleRepository
import com.developer.ivan.domain.*
import kotlinx.coroutines.flow.Flow

class SearchArticles(private val articleRepository: ArticleRepository) :
    UseCase<SearchArticles.Params, List<Article>>() {

    override suspend fun execute(params: Params): Flow<Either<Failure, List<Article>>> =
        articleRepository.searchArticles(params.query, params.page, params.size, params.force)

    class Params(val query: String, val page: Int, val size: Int, val force: Boolean = true)

}