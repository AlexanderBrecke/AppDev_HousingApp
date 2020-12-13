package com.example.housingapp.recycler

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.views.CustomImageLayout
import com.squareup.picasso.Picasso

class ImageScrollAdapter():RecyclerView.Adapter<ImageScrollAdapter.ViewHolder>() {

    private var dataset: MutableList<String> = mutableListOf()
    lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val listItem = CustomImageLayout(context)
//        val listItem:ImageView = ImageView(context)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listItem.setImage(dataset[position])
//        Picasso.get().load(dataset[position]).into(holder.listItem)
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