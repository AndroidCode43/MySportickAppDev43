package com.example.mysportickappdev43.Auth.fragments.CreateAccount

import androidx.lifecycle.ViewModel
import com.example.mysportickappdev43.Auth.utlis.AuthListener
import com.example.mysportickappdev43.utils.isValidDataAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(

) : ViewModel() {

    var authListener : AuthListener? = null

    fun creteNewAccount(email : String, pass : String , confirmPass : String){
        authListener?.let {
            if(email.isEmpty() || pass.isEmpty() || confirmPass.isEmpty()){
                it.onErrorAccount("Поля не должны быть пустыми!")
                return
            }

            if(pass != confirmPass){
                it.onErrorAccount("Пароли не совпадают!")
                return
            }

            if(pass.length < 6){
                it.onErrorAccount("Длинна пароля меньше 6 символов!")
                return
            }

            if(!isValidDataAccount(email)){
                it.onErrorAccount("Email ввдён не верно!")
                return
            }

            it.onSuccessAccount("Регистрация произошла успешно!")
        }
    }
}