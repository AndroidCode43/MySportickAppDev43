package com.example.mysportickappdev43.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = CreatedExerciseEntity.CREATED_EXEC_TABLE)
data class CreatedExerciseEntity(

    @PrimaryKey(autoGenerate = true)
    val id : Int,
    var createTimestamp : String,
    var exerciseName : String,
    var exerciseIcon : String,
    var colorExerciseBg : Int,
    var numberOfTouch : Int = 0,
    var repsPerSetInDay : Int = 0

) : Serializable{
    companion object{
        const val CREATED_EXEC_TABLE = "createdExerciseTable"
    }
}
