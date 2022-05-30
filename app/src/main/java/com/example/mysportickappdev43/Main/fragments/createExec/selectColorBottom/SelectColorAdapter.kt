package com.example.mysportickappdev43.Main.fragments.createExec.selectColorBottom

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysportickappdev43.R

class SelectColorAdapter(
        private val listColor : IntArray,
        private val listener : ColorPickerListener
)  : RecyclerView.Adapter<SelectColorAdapter.SelectColorViewHolder>(){

    class SelectColorViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var itemColor : ImageView = view.findViewById(R.id.item_card_picker)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectColorViewHolder {
        return SelectColorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pick,parent,false))
    }

    override fun onBindViewHolder(holder: SelectColorViewHolder, position: Int) {
        holder.apply {
            itemColor.imageTintList = ColorStateList.valueOf(listColor[position])
            itemView.setOnClickListener {
                listener?.invoke(listColor[position])
            }
        }
    }

    override fun getItemCount(): Int = listColor.size
}