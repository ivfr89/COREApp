package com.developer.ivan.coreapp.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao{

    @Transaction
    @Query("SELECT * FROM DBArticle LIMIT :page, :size")
    fun getAllArticles(page: Int, size: Int) : Flow<List<DBArticleWithRepositories>>

    @Transaction
    @Query("SELECT * FROM DBArticle WHERE id_article=:id")
    fun getArticle(id: Int) : Flow<DBArticleWithRepositories?>

    @Query("SELECT COUNT(*) FROM DBArticle")
    fun getArticlesCount() : Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(dbArticle: List<DBArticle>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateArticle(article: DBArticle)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDBArticleWithRepositories(join: DBArticleRepositoryJoin)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuery(dbArticle: DBQuery)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRepositories(repository: List<DBRepository>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQueryArticlesJoin(repository: List<DBQueryArticleJoin>)





    @Transaction
    suspend fun insertQueryWithArticles(dbQueryArticles: DBQueryWithArticles){

        insertQuery(dbQueryArticles.dbQuery)
        insertArticles(dbQueryArticles.articles.map { it.article })
        insertRepositories(dbQueryArticles.articles.flatMap { it.repositories })

        dbQueryArticles.articles.forEach {structure->

            structure.repositories.forEach { repository->
                insertDBArticleWithRepositories(DBArticleRepositoryJoin(structure.article.id,repository.id))
            }


            dbQueryArticles.dbQuery

        }

        insertQueryArticlesJoin(dbQueryArticles.articles.map {article-> DBQueryArticleJoin(dbQueryArticles.dbQuery.query,article.article.id) })



    }

}