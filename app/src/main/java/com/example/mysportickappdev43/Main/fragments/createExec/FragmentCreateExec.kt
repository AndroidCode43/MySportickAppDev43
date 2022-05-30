package com.example.mysportickappdev43.Main.fragments.createExec

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.example.mysportickappdev43.Main.fragments.createExec.selectColorBottom.ColorPickerBottom
import com.example.mysportickappdev43.Main.fragments.createExec.selectIconBottom.IconPickerBottom
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.databinding.FragmentCreateExecBinding
import com.example.mysportickappdev43.room.DbListener
import com.example.mysportickappdev43.utils.showToastFragment
import com.example.mysportickappdev43.utils.startNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCreateExec : Fragment(R.layout.fragment_create_exec), DbListener{

    private lateinit var mBiding : FragmentCreateExecBinding
    private val mViewModel : CreateExecViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBiding = FragmentCreateExecBinding.bind(view)
        mViewModel.dbListener = this
        initUi()
    }

    private fun initUi(){
        funcBtn()
    }

    private fun btnOpenSheetColorPicker(){
        mBiding.openSheetColor.setOnClickListener{
            ColorPickerBottom()
                .initColorPicker(
                    colors = resources.getIntArray(R.array.colors),
                    listener = {
                        // update color view
                        mViewModel.updateColor(it)
                        mBiding.choseColorView.imageTintList = ColorStateList.valueOf(it)
                    }
                ).show(parentFragmentManager)
        }
    }

    private fun btnOpenSheetIconPicker(){
        mBiding.openSheetIcon.setOnClickListener {
            IconPickerBottom()
                    .initIconPicker(
                            listener = {
                                //update icon view
                                mViewModel.updateIcon(resources.getResourceName(it))
                                mBiding.choseExecIcon.setImageResource(it)
                            }
                    ).show(parentFragmentManager)
        }
    }

    private fun btnCreateExec(){
        mBiding.apply {
            btnCreateExercise.setOnClickListener{
                mViewModel.addNewExecInDb(
                    name = this.inpExecName.text.toString(),
                    repsPerSet = this.inpExecRepOfSet.text.toString(),
                    numberOfTouch = this.inpExecNumbOfTouch.text.toString(),
                )
            }
        }
    }

    private fun btnClose(){
        mBiding.btnExit.setOnClickListener {
            startNavigation(R.id.action_fragmentCreateExec_to_homeFragment)
        }
    }

    private fun funcBtn(){
        btnOpenSheetColorPicker()
        btnOpenSheetIconPicker()
        btnCreateExec()
        btnClose()
    }

    override fun successLoadInDb() {
        showToastFragment("ура")
    }

    override fun failureLoadInDb(msg: String) {
       showToastFragment(msg)
    }
}