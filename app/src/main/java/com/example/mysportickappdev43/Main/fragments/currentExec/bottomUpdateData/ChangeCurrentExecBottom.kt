package com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateData

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateExec.UpdateExerciseListener
import com.example.mysportickappdev43.databinding.UpdateCurrentDataLayoutBinding
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import com.example.mysportickappdev43.utils.KEY_CURRENT_EXERCISE_CHANGE
import com.example.mysportickappdev43.utils.REQUEST_KEY_CHANGE_CURRENT_EXEC
import com.example.mysportickappdev43.utils.showToastFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeCurrentExecBottom : BottomSheetDialogFragment(), UpdateExerciseListener {

    private var bind : UpdateCurrentDataLayoutBinding? = null
    private var currentExecBundle : ExerciseEntity? = null

    private val viewModel : ChangeExecViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //get bundle
        currentExecBundle = requireArguments()
            .getSerializable(KEY_CURRENT_EXERCISE_CHANGE) as ExerciseEntity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = UpdateCurrentDataLayoutBinding.inflate(inflater)
        viewModel.dbListener = this

        btnClick()

        return bind?.root
    }

    private fun btnClick(){
        bind?.apply {
            //click btn save changes
            btnSaveChange.setOnClickListener {
                currentExecBundle?.let {item->
                    viewModel.updateCurrentExec(
                        item = item,
                        reps = this.inpExecRepUpdate.text.toString(),
                        sets = this.inpExecSetUpdateCurrent.text.toString()
                    )
                }
            }

            //click btn close bottom
            btnCloseBottom.setOnClickListener { dialog?.cancel() }
        }
    }

    override fun successUpdateExec(item: Any) {
        bind?.apply {
            inpExecRepUpdate.setText("0")
            inpExecSetUpdateCurrent.setText("0")
        }

        if(item is String){
            setFragmentResult(
                REQUEST_KEY_CHANGE_CURRENT_EXEC,
                bundleOf(KEY_CURRENT_EXERCISE_CHANGE to item)
            )
        }
    }

    override fun failedUpdateExec(msg: String) {
        showToastFragment(msg)
    }
}