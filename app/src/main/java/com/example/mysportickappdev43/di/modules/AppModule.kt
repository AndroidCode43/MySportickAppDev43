package com.example.mysportickappdev43.di.modules

import android.content.Context
import com.example.mysportickappdev43.room.Dao.CreatedExerciseDao
import com.example.mysportickappdev43.room.Dao.CurrentUserDao
import com.example.mysportickappdev43.room.Dao.ExerciseDao
import com.example.mysportickappdev43.room.ExerciseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetExerciseDb(
        @ApplicationContext context : Context
    ) : ExerciseDatabase = ExerciseDatabase.getExerciseDb(context)

    @Provides
    @Singleton
    fun provideGetExecDao(db : ExerciseDatabase) : ExerciseDao = db.getExerciseDao()

    @Provides
    @Singleton
    fun provideGetCurrentUserDao(db : ExerciseDatabase) : CurrentUserDao = db.getCurrentUserDao()

    @Provides
    @Singleton
    fun provideGetCreatedExecDao(db : ExerciseDatabase) : CreatedExerciseDao = db.getCreatedExerciseDao()
}