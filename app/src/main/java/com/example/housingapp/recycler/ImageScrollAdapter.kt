package com.example.housingapp.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.views.CustomImageLayout

class ImageScrollAdapter():RecyclerView.Adapter<ImageScrollAdapter.ViewHolder>() {

    private var dataset: MutableList<String> = mutableListOf()
    lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val listItem = CustomImageLayout(context)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listItem.setImage(dataset[position])
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun setData(newData: MutableList<String>){
        dataset = newData
        notifyDataSetChanged()
    }

    class ViewHolder(val listItem:CustomImageLayout):RecyclerView.ViewHolder(listItem)
}