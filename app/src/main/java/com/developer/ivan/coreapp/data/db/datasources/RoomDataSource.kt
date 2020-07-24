package com.developer.ivan.coreapp.data.db.datasources

import android.util.Log
import com.developer.ivan.coreapp.data.db.ArticleDao
import com.developer.ivan.coreapp.data.db.DB
import com.developer.ivan.coreapp.data.db.QueryDao
import com.developer.ivan.coreapp.data.db.RepositoryDao
import com.developer.ivan.coreapp.data.db.mapper.DBMapper
import com.developer.ivan.data.datasources.LocalDataSource
import com.developer.ivan.domain.Article
import com.developer.ivan.domain.Either
import com.developer.ivan.domain.Failure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class RoomDataSource(
    private val dbMapper: DBMapper,
    database: DB
) : LocalDataSource {

    private val articleDao: ArticleDao = database.articleDao()
    private val repositoryDao: RepositoryDao = database.repositoryDao()
    private val queryDao: QueryDao = database.queryDao()

    object InvalidAmount : Failure.CustomFailure()
    object EmptyList : Failure.CustomFailure()


    override fun searchArticles(
        query: String,
        page: Int,
        size: Int
    ): Flow<Either<Failure, List<Article>>> {
        return queryDao.getAllArticlesByQuery(query, page-1, size)
            .distinctUntilChanged()
            .mapLatest { list ->

                if (list.isEmpty()) Either.Left(EmptyList) else Either.Right(
                    dbMapper.convertDBArticlesToDomain(
                        list.flatMap { it.articles })
                )
            }
            .catch {e->
                Log.e("ROOM", e.localizedMessage)

            }
    }

    override fun getArticle(id: Int): Flow<Either<Failure, Article>> {

        return articleDao.getArticle(id)
            .distinctUntilChanged()
            .mapLatest { article ->

                article?.let {
                    Either.Right(dbMapper.convertDBArticleWithRepositoriesToDomain(it))
                } ?: kotlin.run {
                    Either.Left(EmptyList)
                }
            }
    }

    override fun totalAmount(): Flow<Either<Failure, Int>> {

        return articleDao.getArticlesCount()
            .mapLatest { totalAmount ->
                if (totalAmount > 0) Either.Right(totalAmount) else Either.Left(InvalidAmount)
            }

    }

    override suspend fun insertArticlesWithQuery(query: String, articles: List<Article>) {

        articleDao.insertQueryWithArticles(
            dbMapper.convertQueryArticlesToDB(query, articles)
        )

    }

    override suspend fun updateArticle(article: Article) {

        articleDao.updateArticle(dbMapper.convertArticleToDBArticlesWithRepositories(article).article)


    }
}