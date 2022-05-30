package com.example.mysportickappdev43.Main.fragments.createExec.selectIconBottom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysportickappdev43.R

class SelectIconAdapter(private val iconListener : IconPickerListener) :
        RecyclerView.Adapter<SelectIconAdapter.SelectIconViewHolder>() {

    private val listIcon = listOf(
            R.drawable.ic__icon_run,
            R.drawable.ic_gym,
            R.drawable.ic_cycling,
            R.drawable.ic_lotus_yoga
    )

    class SelectIconViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        var icon : ImageView = view.findViewById(R.id.item_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectIconViewHolder {
        return SelectIconViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_icon,parent,false))
    }

    override fun onBindViewHolder(holder: SelectIconViewHolder, position: Int) {
        holder.apply {
            icon.setImageResource(listIcon[position])
            itemView.setOnClickListener {
                iconListener?.invoke(listIcon[position])
            }
        }
    }

    override fun getItemCount(): Int = listIcon.size
}