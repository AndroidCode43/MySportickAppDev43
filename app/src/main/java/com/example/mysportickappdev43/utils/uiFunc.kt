package com.example.mysportickappdev43.utils

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Color
import android.graphics.Color.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mysportickappdev43.R
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.showToastFragment(msg : String){
    Toast.makeText(this.requireContext(), msg, Toast.LENGTH_SHORT).show()
}

@SuppressLint("SimpleDateFormat")
fun getDate(timeStamp : String) : String {
    return SimpleDateFormat("EEE, d MMM yy").format(Date(timeStamp.toLong()))
}

fun getCalcOfComplete(num1 : Int , num2 : Int) : String = (num1 - num2).toString()

fun TextView.showRemainsValue(repsSetInDay : Int , currentRepsSet : Int){
    val res = repsSetInDay-currentRepsSet
    text = if(res <= 0){
        setTextColor(resources.getColorStateList(R.color.white_red, null))
        resources.getString(R.string.done)
    }else{
        res.toString()
    }
}

fun getCurrentTimestamp() : String = System.currentTimeMillis().toString()

fun getCcalByRepCount(allCount : Int) : String{
    return (allCount * 0.9).toInt().toString()
}