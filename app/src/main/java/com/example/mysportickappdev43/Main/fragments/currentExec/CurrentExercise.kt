 package com.example.mysportickappdev43.Main.fragments.currentExec

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateData.ChangeCurrentExecBottom
import com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateExec.UpdateExecBottom
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentCurrentExerciseBinding
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import com.example.mysportickappdev43.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentExercise : Fragment(R.layout.fragment_current_exercise),
    PopupMenu.OnMenuItemClickListener, CurrentExerciseListener{

    private val viewModel : CurrentExerciseViewModel by viewModels()

    private var mBiding : FragmentCurrentExerciseBinding? = null
    private var currentCreatedExecBundle : CreatedExerciseEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBiding = FragmentCurrentExerciseBinding.bind(view)
        init()
    }

    private fun init(){
        viewModel.currentExecListener = this
        currentCreatedExecBundle = requireArguments()
            .getSerializable(KEY_CURRENT_EXERCISE_ID) as CreatedExerciseEntity

        initBundle()
        initObservers()
        initBtn()
    }

    private fun initBundle(){
        currentCreatedExecBundle?.let { entity ->
            initFieldsForBundle(entity)

            viewModel.apply {
                getCurrentExecListByName(entity.exerciseName)
            }
        }
        return
    }
 
    private fun initObservers(){
        viewModel.apply {
            //get all exercise list
            listExecLiveData.observe(viewLifecycleOwner) {
                it?.let {list->
                    mBiding?.let {bind->
                        bind.txCountAllDay.text = list.size.toString()
                        bind.dateView.adapter = CurrentExecAdapter(list)
                    }
                }
            }

            //observer current exec
            getCurrentExecLiveData.observe(viewLifecycleOwner) {
                mBiding?.apply {
                    initStaticsFields(it)
                    initTaskFields(it)
                }
            }
        }

        //observe update current exec
        setFragmentResultListener(REQUEST_KEY_UPDATE_CURRENT_EXEC){ _, bundle ->
            bundle.getSerializable(KEY_CURRENT_EXERCISE_UPDATE)?.let {
                if(it is String){
                    viewModel.getCurrentExecListByName(it)
                }
            }
        }

        setFragmentResultListener(REQUEST_KEY_CHANGE_CURRENT_EXEC){_,bundle ->
            bundle.getSerializable(KEY_CURRENT_EXERCISE_CHANGE)?.let {
                if(it is String){
                    viewModel.getCurrentExecListByName(it)
                }
            }
        }
    }

    private fun initStaticsFields(model : ExerciseEntity){
        mBiding?.apply {
            txNameExec.text = model.nameExercise
            txDate.text = getDate(model.timestamp)
            txCountRepetitions.text = model.currentRepsPerSet.toString()
            txCountApproaches.text = model.currentNumberOfTouch.toString()
            txLeftToComplete.showRemainsValue(model.repsPerSetInDay,model.currentRepsPerSet)
            txCcalBurned.text = getCcalByRepCount(model.currentRepsPerSet)


            circularProgressIndicator.apply {
                progress = model.currentRepsPerSet
                max = model.repsPerSetInDay
            }
        }
    }

    private fun initTaskFields(model : ExerciseEntity){
        mBiding?.apply {
            txCountApproachesTask.text = model.numberOfTouch.toString()
            txCountRepetitionsTask.text = model.repsPerSetInDay.toString()
        }
    }

    private fun initChipClick(){
        mBiding!!.chipMadeApproach.setOnClickListener{

            viewModel.listExecLiveData.value?.let { list->
                if(list.isNotEmpty()){
                    UpdateExecBottom().apply {
                        arguments = Bundle().apply {
                            putSerializable(KEY_CURRENT_EXERCISE_UPDATE, list.last())
                        }

                    }.show(parentFragmentManager, "")
                    return@setOnClickListener
                }
            }

            showToastFragment("NULL")
            currentCreatedExecBundle?.let {
                viewModel.createExec(it)
            }
            return@setOnClickListener
        }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        when(p0!!.itemId){
                R.id.menu_delete_current_exec -> showToastFragment("Удаление!")
        }
        return false
    }

    private fun initBtnPopMenu(){
        mBiding!!.btnShowOptionsMenu.setOnClickListener {
            showPopMenu()
        }
    }

    private fun initBtnUpdateData(){
        mBiding?.apply {
            btnUpdateCurrentData.setOnClickListener{
                viewModel.listExecLiveData.value?.let {
                    ChangeCurrentExecBottom().apply {
                        arguments = Bundle().apply {
                            putSerializable(KEY_CURRENT_EXERCISE_CHANGE, it.last())
                        }
                    }.show(parentFragmentManager,"")
                }
            }
        }
    }

    private fun showPopMenu(){
        PopupMenu(requireContext(), requireView()).apply {
            inflate(R.menu.exec_pop_menu)
            setOnMenuItemClickListener(this@CurrentExercise)
        }.show()
    }

    private fun initFieldsForBundle(entity : CreatedExerciseEntity){
        mBiding?.apply {
            this.txCountRepetitionsTask.text = entity.repsPerSetInDay.toString()
            this.txCountApproachesTask.text = entity.numberOfTouch.toString()
            this.txNameExec.text = entity.exerciseName
            this.txDate.text = getDate(entity.createTimestamp)
        }
    }

    override fun nullDataDailyTask(msg: String) {
        showSnackBar(msg,true)
    }

    private fun initBtnExit(){
        mBiding!!.btnExitExercise.setOnClickListener {
            startNavigation(R.id.action_currentExercise_to_homeFragment)
        }
    }

    private fun initBtn(){
        initBtnUpdateData()
        initChipClick()
        initBtnPopMenu()
        initBtnExit()
    }
}