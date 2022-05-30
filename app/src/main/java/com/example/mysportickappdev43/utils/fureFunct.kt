package com.example.mysportickappdev43.utils

import android.util.Patterns

fun isValidDataAccount(email : String) : Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}