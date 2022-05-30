 package com.example.mysportickappdev43.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = ExerciseEntity.TABLE_NAME)
data class ExerciseEntity(

        @PrimaryKey(autoGenerate = true)
        val id : Int = 0,
        var nameExercise : String,
        var timestamp : String,
    //повторения за один подход
        var currentRepsPerSet : Int = 0,
        var repsPerSetInDay : Int = 0,
    //кол-во подходов
        var numberOfTouch : Int = 0,
        var currentNumberOfTouch : Int = 0

    ) : Serializable
{
    companion object{
        const val TABLE_NAME = "exercisesTable"
    }
}