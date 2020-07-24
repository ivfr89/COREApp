package com.developer.ivan.usecases

import com.developer.ivan.data.repositories.ArticleRepository
import com.developer.ivan.domain.*
import kotlinx.coroutines.flow.Flow

class GetArticle(private val articleRepository: ArticleRepository) :
    UseCase<GetArticle.Params, Article>() {

    override suspend fun execute(params: Params): Flow<Either<Failure, Article>> =
        articleRepository.getArticle(params.id,params.force)


    class Params(val id: Int, val force: Boolean)

}