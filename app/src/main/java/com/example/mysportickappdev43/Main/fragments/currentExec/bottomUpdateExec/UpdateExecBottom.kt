package com.example.mysportickappdev43.Main.fragments.currentExec.bottomUpdateExec

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.mysportickappdev43.databinding.UpdateTheExecLayoutBinding
import com.example.mysportickappdev43.room.entity.ExerciseEntity
import com.example.mysportickappdev43.utils.KEY_CURRENT_EXERCISE_UPDATE
import com.example.mysportickappdev43.utils.REQUEST_KEY_UPDATE_CURRENT_EXEC
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateExecBottom : BottomSheetDialogFragment(), UpdateExerciseListener{

    private val viewModel : UpdateExecViewModel by viewModels()

    private var binding : UpdateTheExecLayoutBinding? = null
    private var execBundle : ExerciseEntity? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = UpdateTheExecLayoutBinding.inflate(inflater)
        viewModel.updateListener = this

        initUi()

        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundle()
    }

    private fun getBundle(){
        execBundle = requireArguments()
                .getSerializable(KEY_CURRENT_EXERCISE_UPDATE) as ExerciseEntity
    }

    private fun initUi(){

        if(execBundle==null){
            return
        }
        execBundle?.let { model ->
            binding?.let {bind->
                bind.btnSaveChange.setOnClickListener {
                    viewModel.updateCurrentExec(
                            model,
                            bind.inpExecRepOfSetUpdate.text.toString().toInt()
                    )
                }

                //close btn
                bind.btnCloseBottom.setOnClickListener {
                    dialog?.cancel()
                }
            }
        }
        return
    }

    override fun successUpdateExec(item: Any) {
        binding!!.inpExecRepOfSetUpdate.setText("0")

        if(item is String){
            setFragmentResult(REQUEST_KEY_UPDATE_CURRENT_EXEC,
                bundleOf(KEY_CURRENT_EXERCISE_UPDATE to item)
            )
        }
    }

    override fun failedUpdateExec(msg: String) {

    }
}