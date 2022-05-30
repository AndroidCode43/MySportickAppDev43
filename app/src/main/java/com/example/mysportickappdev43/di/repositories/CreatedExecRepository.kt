package com.example.mysportickappdev43.di.repositories

import com.example.mysportickappdev43.room.Dao.CreatedExerciseDao
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CreatedExecRepository @Inject constructor(
    private val createdExerciseDao : CreatedExerciseDao
) {

    fun getAllCreatedExec() : Single<List<CreatedExerciseEntity>> = createdExerciseDao.getAllCreatedExec()

    fun updateCreatedExec(exec : CreatedExerciseEntity) : Completable =
        createdExerciseDao.updateCreatedExec(exec)

    fun insertCreatedExec(exec : CreatedExerciseEntity) : Completable =
        createdExerciseDao.insertCreatedExec(exec)
}