package com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateExec

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.mysportickappdev43.di.repositories.ExerciseDbRepository
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import com.example.mysportickappdev43.utils.getCurrentTimestamp
import com.example.mysportickappdev43.utils.getDate
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class UpdateExecViewModel @Inject constructor(
        application: Application,
        private val repository : ExerciseDbRepository
) : AndroidViewModel(application) {

    private var dispose = CompositeDisposable()
    var updateListener : UpdateExerciseListener? = null

    fun updateCurrentExec(item : ExerciseEntity, newValue : Int){
        if(newValue == 0){

            //TODO предупреждение
            return
        }


        if(getDate(item.timestamp) == getDate(getCurrentTimestamp())){
            dispose.add(
                repository
                    .updateExercise(item.apply {

                        currentRepsPerSet += newValue
                        currentNumberOfTouch += 1

                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        updateListener?.successUpdateExec(item.nameExercise)
                    }, { Toast.makeText(getApplication(), it.message.toString(), Toast.LENGTH_SHORT).show() })
            )

            return
        }

        dispose.add(
            repository
                .insertExercise(
                    ExerciseEntity(
                        id = 0,
                        nameExercise = item.nameExercise,
                        timestamp = System.currentTimeMillis().toString(),
                        currentRepsPerSet = newValue,
                        numberOfTouch = 1,
                        currentNumberOfTouch = 0,
                        repsPerSetInDay = 0
                    )
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    updateListener?.successUpdateExec(item.nameExercise)
                }, { Toast.makeText(getApplication(), it.message.toString(), Toast.LENGTH_SHORT).show() })
        )
    }

//    private fun getCurrentExec(itemId : Int){
//        dispose.add(
//               repository
//                       .getItemById(itemId)
//                       .subscribeOn(Schedulers.newThread())
//                       .observeOn(AndroidSchedulers.mainThread())
//                       .subscribe({updateListener?.successUpdateExec(it)},{})
//        )
//    }
}