package com.developer.ivan.coreapp.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QueryDao{

    @Query("SELECT * FROM DBQuery WHERE `query`=:query LIMIT :page, :size")
    fun getAllArticlesByQuery(query: String, page: Int, size: Int) : Flow<List<DBQueryWithArticles>>

    @Query("SELECT COUNT(*) FROM DBQuery")
    fun getQueriesCount() : Flow<Int>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateQuery(article: DBQuery)

}