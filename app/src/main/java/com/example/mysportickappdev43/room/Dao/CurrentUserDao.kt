package com.example.mysportickappdev43.room.Dao

import androidx.room.*
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrentUserDao {

    @Query("SELECT * FROM ${CurrentUserEntity.CURRENT_USER_TABLE_NAME} WHERE id = 1")
    fun getCurrentUser() : Single<CurrentUserEntity>

    @Insert(entity = CurrentUserEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertCurrentUser(user : CurrentUserEntity) : Completable

    @Update(entity = CurrentUserEntity::class)
    fun updateCurrentUser(user : CurrentUserEntity) : Completable
}