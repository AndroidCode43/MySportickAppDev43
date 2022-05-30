package com.example.mysportickappdev43.Main.fragments.createExec.selectColorBottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.example.mysportickappdev43.databinding.SecetColorLayoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias ColorPickerListener = ((color : Int) -> Unit)?

class ColorPickerBottom : BottomSheetDialogFragment() {

    private lateinit var mBinding : SecetColorLayoutBinding

    private var colorAdapter : SelectColorAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = SecetColorLayoutBinding.inflate(inflater)

        //init ui
        mBinding.apply {
            //init btn close
            btnClose.setOnClickListener {
                dialog?.cancel()
            }

            if(colorAdapter != null){
                this.colorRecyclerView.adapter = colorAdapter
            }
        }

        return mBinding.root
    }

    fun initColorPicker(
            listener : ColorPickerListener,
            colors : IntArray
    ) : ColorPickerBottom{
        colorAdapter = SelectColorAdapter(
                listColor = colors,
                listener = listener
        )
        return this
    }

    fun show(fg : FragmentManager){
        this.show(fg,"")
    }
}