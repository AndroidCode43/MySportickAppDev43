package com.example.mysportickappdev43.utils

import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.mysportickappdev43.R
import com.google.android.material.snackbar.Snackbar

fun Fragment.startNavigation(action : Int){
    Navigation.findNavController(requireView())
        .navigate(action)
}

fun Fragment.startNavigationWithBundle(action: Int, bundle : Bundle){
    Navigation.findNavController(requireView())
        .navigate(action,bundle)
}

fun Fragment.showSnackBar(msg : String, isLong : Boolean){
    if(isLong){
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).apply {
            changeTextColorSnackBar(R.color.green_neon_color)
        }.show()
        return
    }
    Snackbar.make(requireView(), msg, Snackbar.LENGTH_SHORT).apply {
        changeTextColorSnackBar(R.color.green_neon_color)
    }.show()
}

fun Snackbar.changeTextColorSnackBar(color : Int){
    view.apply {
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_main_gray))
        findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            .setTextColor(ContextCompat.getColor(context,color))
    }
}