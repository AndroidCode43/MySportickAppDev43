package com.example.mysportickappdev43.di.repositories

import com.example.mysportickappdev43.room.Dao.ExerciseDao
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ExerciseDbRepository @Inject constructor(
    private val provideExecDao : ExerciseDao
) {

    fun getAllListExercise() : Single<List<ExerciseEntity>>{
        return provideExecDao.getAllCreatedExercise()
    }

    fun getAllListCurrentExercise(nameExec : String) : Single<List<ExerciseEntity>>{
        return provideExecDao.getAllListCurrentExercise(nameExec)
    }

    fun insertExercise(item : ExerciseEntity) : Completable{
        return provideExecDao.addExerciseInDb(item)
    }

    fun deleteExercise(item : ExerciseEntity) : Completable{
        return provideExecDao.deleteExercise(item)
    }

    fun updateExercise(item : ExerciseEntity) : Completable{
        return provideExecDao.updateExercise(item)
    }

    fun getItemById(itemId : Int) : Single<ExerciseEntity>{
        return provideExecDao.getItemById(itemId)
    }
}