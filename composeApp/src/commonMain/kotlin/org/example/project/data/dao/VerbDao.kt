package org.example.project.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VerbDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateList(verbs: List<VerbEntity>): LongArray

    @Query("SELECT * FROM verbs")
    fun getAllVerbs(): Flow<List<VerbEntity>>

    @Query("SELECT * FROM verbs WHERE isDone = false")
    fun getAllVerbsNotDone(): Flow<List<VerbEntity>>

    @Query("UPDATE verbs SET isDone = :isDone WHERE present = :name")
    suspend fun updateIsDone(name: String, isDone: Boolean): Int

}