package com.developer.ivan.coreapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DBArticle::class, DBRepository::class, DBArticleRepositoryJoin::class, DBQuery::class, DBQueryArticleJoin::class],
    version = 1
)
abstract class DB : RoomDatabase() {

    abstract fun articleDao(): ArticleDao
    abstract fun queryDao(): QueryDao
    abstract fun repositoryDao(): RepositoryDao

}
