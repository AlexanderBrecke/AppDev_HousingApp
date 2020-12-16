package com.example.housingapp.recycler

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.extras.indicator.Indicator
import com.example.housingapp.views.CustomImageLayout

class ImageScrollAdapter():RecyclerView.Adapter<ImageScrollAdapter.ViewHolder>() {

    private var dataset: MutableList<String> = mutableListOf()
    lateinit var context:Context

    // --- Standard adapter setup ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val listItem = CustomImageLayout(context)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listItem.setImage(dataset[position])
        padItems(holder.listItem, position)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
    // ---

    //Hack to pad the items when they are bound.
    //I did not find a better way to do this, and it feels a bit dirty
    //We will measure the view, with unspecified specs.
    //Then we will set the padding to 1/4 of the measured width.
    //We will then set the padding to the left and right side of the view.
    //If it is the first position, we will set twice the padding on the left side,
    //and if it is the last, we will do the same but for the right side.
    private fun padItems(view:View, position: Int){
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val sidePadding = (view.measuredWidth/4)
        when(position){
            0 -> view.setPadding((sidePadding*2),0,sidePadding,0)
            (dataset.size - 1) -> view.setPadding(sidePadding,0,(sidePadding*2),0)
            else -> view.setPadding(sidePadding,0,sidePadding,0)
        }
    }

    //We will have a function to set the data set of the adapter.
    //This is because we will now initialize the adapter from a custom class,
    //and the custom class will do some logic when the data changes.
    fun setData(newData: MutableList<String>){
        dataset = newData
        notifyDataSetChanged()
    }

    class ViewHolder(val listItem:CustomImageLayout):RecyclerView.ViewHolder(listItem)
}