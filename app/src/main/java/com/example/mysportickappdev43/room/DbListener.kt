package com.example.mysportickappdev43.room

interface DbListener {
    fun successLoadInDb()
    fun failureLoadInDb(msg : String)
}