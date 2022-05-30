package com.example.mysportickappdev43.Main.fragments.home

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.example.mysportickappdev43.di.repositories.CreatedExecRepository
import com.example.mysportickappdev43.di.repositories.CurrentUserRepository
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.room.entity.CurrentUserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class  HomeViewModel @Inject constructor(
        application : Application,
        private val dbRepository : CreatedExecRepository,
        private val currentUserRepository: CurrentUserRepository
) : AndroidViewModel(application), LifecycleEventObserver{

    private var mDispose = CompositeDisposable()

    var getAllExerciseLiveData = MutableLiveData<List<CreatedExerciseEntity>>()
    var currentUserLiveData = MutableLiveData<CurrentUserEntity>()

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event){
            Lifecycle.Event.ON_START -> {
                mDispose.addAll(
                    currentUserRepository
                        .getCurrentUser()
                        .subscribeOn(Schedulers.newThread())
                        .subscribe({
                                   currentUserLiveData.postValue(it)
                        },{}),

                        dbRepository
                                .getAllCreatedExec()
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    getAllExerciseLiveData
                                            .postValue(it)
                                }, {
                                    Toast.makeText(getApplication(), it.message.toString(), Toast.LENGTH_SHORT).show()
                                })
                )
            }

            else -> return
        }
    }
}