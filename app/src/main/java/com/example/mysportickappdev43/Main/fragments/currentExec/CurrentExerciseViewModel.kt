package com.example.mysportickappdev43.Main.fragments.currentExec

import android.app.Application
import androidx.lifecycle.*
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.di.repositories.ExerciseDbRepository
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class CurrentExerciseViewModel @Inject constructor(
        application: Application,
        private val repository : ExerciseDbRepository
) : AndroidViewModel(application){

    private var compDispose = CompositeDisposable()

    var listExecLiveData = MutableLiveData<List<ExerciseEntity>>().also {
        it.value = null
    }
    var getCurrentExecLiveData = MutableLiveData<ExerciseEntity>()

    var currentExecListener : CurrentExerciseListener? = null

    fun getCurrentExecListByName(nameExec : String){
        compDispose.add(
            repository
                .getAllListCurrentExercise(nameExec)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    listExecLiveData.postValue(it)

                    if(it.isNotEmpty()){
                        getCurrentExecLiveData.postValue(it.last())

                        if(it.last().numberOfTouch == 0) {
                            currentExecListener?.nullDataDailyTask(
                                getApplication<Application>().getString(
                                    R.string.null_daily_task
                                )
                            )
                        }
                    }

                },{})
        )
    }

    fun createExec(entity : CreatedExerciseEntity){
        compDispose.add(
            repository.insertExercise(
                ExerciseEntity(
                    id = 0,
                    nameExercise = entity.exerciseName,
                    timestamp = entity.createTimestamp,
                    currentRepsPerSet = 0,
                    numberOfTouch = entity.numberOfTouch,
                    currentNumberOfTouch = 0,
                    repsPerSetInDay = entity.repsPerSetInDay

                )
            ).subscribeOn(Schedulers.newThread())
                .subscribe({
                    getCurrentExecListByName(entity.exerciseName)
                },{})
        )
    }
}