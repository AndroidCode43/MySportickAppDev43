package com.example.mysportickappdev43.Main.fragments.createExec

import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mysportickappdev43.di.repositories.CreatedExecRepository
import com.example.mysportickappdev43.room.DbListener
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CreateExecViewModel @Inject constructor(
    private val createdExecRepository : CreatedExecRepository
) : ViewModel(){

    private var mDispose = CompositeDisposable()

    var dbListener : DbListener? = null

    private var colorLiveData = MutableLiveData<Int>().also {
        it.value = Color.parseColor("#202020")
    }

    private var iconLiveData = MutableLiveData<String>().also {
        it.value = null
    }

    fun addNewExecInDb(
        name : String,
        repsPerSet : String,
        numberOfTouch : String,
    ){
        if(name.isEmpty()){
            dbListener?.failureLoadInDb("поле не должно быть пустым!")
            return
        }

        if(repsPerSet.isEmpty() || repsPerSet.toInt() < 1){
            dbListener?.failureLoadInDb("поле не должно быть пустым!")
            return
        }

        if(numberOfTouch.isEmpty() || numberOfTouch.toInt() < 1){
            dbListener?.failureLoadInDb("поле не должно быть пустым!")
            return
        }

        if(iconLiveData.value == null){
            dbListener?.failureLoadInDb("Вы должны выбрать иконку для упражнения!")
            return
        }

        mDispose.add(
            createdExecRepository.insertCreatedExec(
                CreatedExerciseEntity(
                    id = 0,
                    exerciseName = name.trim().trimEnd(),
                    createTimestamp = System.currentTimeMillis().toString(),
                    numberOfTouch = numberOfTouch.toInt(),
                    colorExerciseBg = colorLiveData.value!!,
                    exerciseIcon = iconLiveData.value!!,
                    repsPerSetInDay = repsPerSet.toInt()
                )
            ).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {dbListener?.successLoadInDb()},
                    {dbListener?.failureLoadInDb(it.localizedMessage!!.toString())}
                )
        )
    }

    fun updateColor(color : Int){
        colorLiveData.postValue(color)
    }

    fun updateIcon(name_icon : String){
        iconLiveData.postValue(name_icon)
    }
}