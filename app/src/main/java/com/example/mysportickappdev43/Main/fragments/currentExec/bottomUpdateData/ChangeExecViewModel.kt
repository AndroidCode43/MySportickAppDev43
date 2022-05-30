package com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateExec.UpdateExerciseListener
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.di.repositories.ExerciseDbRepository
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ChangeExecViewModel @Inject constructor(
    private val execRepository: ExerciseDbRepository,
    application: Application
) : AndroidViewModel(application) {

    private var dispose = CompositeDisposable()
    var dbListener : UpdateExerciseListener? = null


    fun updateCurrentExec(item : ExerciseEntity, reps : String, sets : String){
        if(reps.isEmpty() || sets.isEmpty()){
            dbListener?.failedUpdateExec(
                getApplication<Application>().getString(R.string.empty_fields)
            )
            return
        }

        if(reps.toInt() < 3){
            dbListener?.failedUpdateExec(
                getApplication<Application>().getString(R.string.less_than_3)
            )
            return
        }

        if(sets.toInt() < 2){
            dbListener?.failedUpdateExec(
                getApplication<Application>().getString(R.string.reps_more_2)
            )
            return
        }

        dispose.add(
            execRepository
                .updateExercise(
                    item.apply {
                        repsPerSetInDay = reps.toInt()
                        numberOfTouch = sets.toInt()
                    }
                )
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                           dbListener?.successUpdateExec(item.nameExercise)
                },{
                    dbListener?.failedUpdateExec(it.message.toString())
                })
        )
    }
}