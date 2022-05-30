package com.example.mysportickappdev43.Main.fragments.currentExec

import com.example.mysportickappdev43.room.entity.ExerciseEntity
import com.robinhood.spark.SparkAdapter

class CurrentExecAdapter(
    private val listData : List<ExerciseEntity>
) : SparkAdapter() {
    override fun getCount(): Int = listData.size

    override fun getItem(p0: Int): kotlin.Any = listData[p0]

    override fun getY(p0: Int): Float {
        return listData[p0].currentRepsPerSet.toFloat()
    }
}