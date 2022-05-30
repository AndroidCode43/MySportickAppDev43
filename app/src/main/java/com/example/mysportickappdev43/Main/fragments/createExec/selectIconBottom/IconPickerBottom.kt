package com.example.mysportickappdev43.Main.fragments.createExec.selectIconBottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.mysportickappdev43.databinding.SelectIconLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias IconPickerListener = ((icon : Int) -> Unit)?
class IconPickerBottom : BottomSheetDialogFragment() {

    private lateinit var mBinding : SelectIconLayoutBinding
    private var iconAdapter : SelectIconAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = SelectIconLayoutBinding.inflate(inflater)

        mBinding.apply {
            //init adapter
            if(iconAdapter!=null){
                this.iconRecyclerView.adapter = iconAdapter
            }

            btnClose.setOnClickListener {
                dialog?.cancel()
            }
        }

        return mBinding.root
    }

    fun initIconPicker(listener : IconPickerListener) : IconPickerBottom{
        iconAdapter = SelectIconAdapter(
                iconListener = listener
        )
        return this
    }

    fun show(fg : FragmentManager){
        this.show(fg,"")
    }
}