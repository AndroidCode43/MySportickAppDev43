package com.example.mysportickappdev43.Main.fragments.home

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mysportickappdev43.R
import com.example.mysportickappdev43.room.entity.CreatedExerciseEntity
import com.example.mysportickappdev43.utils.RecyclerClickListener
import com.example.mysportickappdev43.utils.START_CREATE_EXEC
import com.google.android.material.card.MaterialCardView

class ExecRecyclerView(
        private val listExec : List<CreatedExerciseEntity>,
        val currentRes : Resources
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        var recyclerClickListener : RecyclerClickListener? = null
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEMS = 1
    }

    private class HeaderViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var iconCreate : MaterialCardView = view.findViewById(R.id.itemCreate)

        fun bind(){
            iconCreate.setOnClickListener {
                recyclerClickListener?.clickItem(START_CREATE_EXEC)
            }
        }
    }

    private class ItemViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var color : MaterialCardView = view.findViewById(R.id.itemCreate)
        var name : TextView = view.findViewById(R.id.itemExecName)
        var icon : ImageView = view.findViewById(R.id.itemExecIcon)

        fun bind(item : CreatedExerciseEntity,currentRes: Resources){
            color.setCardBackgroundColor(item.colorExerciseBg)
            name.text = item.exerciseName
            icon.setImageResource(
                currentRes.getIdentifier(
                    item.exerciseIcon,
                    "drawable",
                    "com.example.mysportickappdev43")
            )

            itemView.setOnClickListener {
                recyclerClickListener?.clickItem(item)
            }
        }
    }

    override fun getItemCount(): Int = listExec.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeaderViewHolder -> holder.bind()
            is ItemViewHolder -> holder.bind(listExec[position - 1],currentRes)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position == 0){return TYPE_HEADER}
        return TYPE_ITEMS
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_HEADER -> HeaderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.header_layout_home,parent,false))
            TYPE_ITEMS -> ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_home_your_exec_layout,parent,false))
            else -> throw Exception("Invalid type")
        }
    }
}