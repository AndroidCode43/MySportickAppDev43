package com.example.mysportickappdev43.Auth.fragments.SignIn

import androidx.lifecycle.ViewModel
import com.example.mysportickappdev43.Auth.utlis.AuthListener
import com.example.mysportickappdev43.utils.isValidDataAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(

) : ViewModel(){

    var authListener : AuthListener? = null

    fun signInAccount(email : String , pass : String){
        authListener?.let{
            if(email.isEmpty() || pass.isEmpty()){
                it.onErrorAccount("Поля не должны быть пустыми!")
                return
            }

            if(!isValidDataAccount(email)){
                it.onErrorAccount("Email ввдён не верно!")
                return
            }

            if(pass.length < 6){
                it.onErrorAccount("Длинна пароля меньше 6 символов!")
                return
            }

            it.onSuccessAccount("Вход в аккаунт произошёл успешно!")
        }
    }
}