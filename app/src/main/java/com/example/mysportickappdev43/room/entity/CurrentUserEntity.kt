package com.example.mysportickappdev43.room.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = CurrentUserEntity.CURRENT_USER_TABLE_NAME)
data class CurrentUserEntity(

    @PrimaryKey(autoGenerate = false)
    val id : Int = 1,
    var userNick : String,
    var userHeight : Int,
    var userWeight : Int,
    var userBirthday : String,
    var userPhoto : Bitmap

) : Serializable
{
    companion object{
        const val CURRENT_USER_TABLE_NAME = "currentUserTable"
    }
}