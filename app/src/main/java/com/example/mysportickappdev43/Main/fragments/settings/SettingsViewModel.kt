package com.example.mysportickappdev43.Main.fragments.settings

import android.app.Application
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.*
import com.example.mysportickappdev43.di.repositories.CurrentUserRepository
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repositoryDb : CurrentUserRepository,
    application : Application
) : AndroidViewModel(application), LifecycleEventObserver{

    private var dispose = CompositeDisposable()
    private var isCreated = false

    var userDataLiveData = MutableLiveData<CurrentUserEntity>()


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_START -> {
               getUserRx()
            }
            else -> return
        }
    }

    fun updateUserProfile(
        nick : String,
        height : String,
        weight : String,
        birthday : String
    ){
        if(nick == "" || height == "" || birthday == "" || weight == ""){
            Toast.makeText(getApplication(), "Поля не должны быть пустыми!", Toast.LENGTH_SHORT).show()
            return
        }

        if(!isCreated){
            createUserRx(nick, height, weight, birthday)
        }else{
            updateUserRx(nick, height, weight,birthday)
        }
    }

    private fun updateUserRx(nick : String, height : String, weight : String, age : String){
        dispose.add(
            repositoryDb
                .updateCurrentUser(
                    CurrentUserEntity(
                        userNick = nick,
                        userHeight = height.toInt(),
                        userWeight = weight.toInt(),
                        //def bitmap
                        userPhoto = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888),
                        userBirthday = age
                    )
                ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isCreated = true
                    getUserRx()
                },{
                    Toast.makeText(getApplication(), it.message.toString(), Toast.LENGTH_SHORT).show()
                })
        )
    }

    private fun createUserRx(nick : String, height : String, weight : String, age : String){
        dispose.add(
            repositoryDb
                .insertCurrentUser(
                    CurrentUserEntity(
                        userNick = nick,
                        userHeight = height.toInt(),
                        userWeight = weight.toInt(),
                        //def bitmap
                        userPhoto = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888),
                        userBirthday = age
                    )
                ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isCreated = true
                    getUserRx()
                },{
                    Toast.makeText(getApplication(), it.message.toString(), Toast.LENGTH_SHORT).show()
                })
        )
    }

    private fun getUserRx(){
        dispose.add(
            repositoryDb
                .getCurrentUser()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    isCreated = true
                    userDataLiveData.postValue(it)
                },{
                    Toast.makeText(getApplication(), "ПОШЁЛ НА ХУЙ!", Toast.LENGTH_SHORT).show()
                })
        )
    }
}