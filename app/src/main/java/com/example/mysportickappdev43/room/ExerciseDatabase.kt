package com.example.mysportickappdev43.room

import android.content.Context
import androidx.room.*
import com.example.mysportickappdev43.room.Dao.CreatedExerciseDao
import com.example.mysportickappdev43.room.Dao.CurrentUserDao
import com.example.mysportickappdev43.room.Dao.ExerciseDao
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import com.example.mysportickappdev43.utils.Converter

@TypeConverters(Converter::class)
@Database(
        entities = [ExerciseEntity::class, CurrentUserEntity::class,CreatedExerciseEntity::class],
        version = 1,
        exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase(){

        abstract fun getExerciseDao() : ExerciseDao
        abstract fun getCurrentUserDao() : CurrentUserDao
        abstract fun getCreatedExerciseDao() : CreatedExerciseDao

        companion object{
                private var dbINSTANCE : ExerciseDatabase? = null

                fun getExerciseDb(context : Context) : ExerciseDatabase{
                        if(dbINSTANCE == null){
                                dbINSTANCE = Room
                                        .databaseBuilder(
                                                context.applicationContext,
                                                ExerciseDatabase::class.java,
                                                "exercise_db")
                                        .allowMainThreadQueries()
                                        .build()
                        }
                        return dbINSTANCE!!
                }
        }
}