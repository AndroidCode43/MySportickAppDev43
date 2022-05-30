package com.example.mysportickappdev43.room.Dao

import androidx.room.*
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM ${ExerciseEntity.TABLE_NAME} ORDER BY id DESC")
    fun getAllCreatedExercise() : Single<List<ExerciseEntity>>

    @Query("SELECT * FROM ${ExerciseEntity.TABLE_NAME} WHERE nameExercise = :nameExercise")
    fun getAllListCurrentExercise(nameExercise : String) : Single<List<ExerciseEntity>>

    @Query("SELECT * FROM ${ExerciseEntity.TABLE_NAME} WHERE id = :itemId")
    fun getItemById(itemId : Int) : Single<ExerciseEntity>

    @Insert(entity = ExerciseEntity::class, onConflict = OnConflictStrategy.ABORT)
    fun addExerciseInDb(exerciseItem : ExerciseEntity) : Completable

    @Delete(entity = ExerciseEntity::class)
    fun deleteExercise(exerciseItem : ExerciseEntity) : Completable

    @Update(entity = ExerciseEntity::class)
    fun updateExercise(exerciseItem: ExerciseEntity) : Completable
}