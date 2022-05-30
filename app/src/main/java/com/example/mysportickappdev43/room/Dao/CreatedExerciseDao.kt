package com.example.mysportickappdev43.room.Dao

import androidx.room.*
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

@Dao
interface CreatedExerciseDao {

    @Query("SELECT * FROM ${CreatedExerciseEntity.CREATED_EXEC_TABLE} ORDER BY id DESC")
    fun getAllCreatedExec() : Single<List<CreatedExerciseEntity>>

    @Insert(entity = CreatedExerciseEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertCreatedExec(exec : CreatedExerciseEntity) : Completable

    @Update(entity = CreatedExerciseEntity::class)
    fun updateCreatedExec(exec : CreatedExerciseEntity) : Completable

}