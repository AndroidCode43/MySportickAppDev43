package com.example.mysportickappdev43.di.repositories

import com.example.mysportickappdev43.room.Dao.CurrentUserDao
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class CurrentUserRepository @Inject constructor(
    private val currentUserDao: CurrentUserDao
) {

    fun getCurrentUser() : Single<CurrentUserEntity> = currentUserDao.getCurrentUser()

    fun updateCurrentUser(user : CurrentUserEntity) : Completable =
        currentUserDao.updateCurrentUser(user)

    fun insertCurrentUser(user : CurrentUserEntity) : Completable =
        currentUserDao.insertCurrentUser(user)
}