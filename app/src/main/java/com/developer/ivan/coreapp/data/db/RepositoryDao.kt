package com.developer.ivan.coreapp.data.db

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface RepositoryDao{

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateRepository(repository: DBRepository)

}