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
//    var dataViewList = mutableListOf<View>()
    var holderList:HashMap<Int, View> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val listItem = CustomImageLayout(context)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.listItem.setImage(dataset[position])
        padItems(holder.listItem, position)
        if(!holderList.containsKey(position)) holderList.put(position,holder.listItem)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    //Hack in a padding on the items
    private fun padItems(view:View, position: Int){
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val sidePadding = (view.measuredWidth/4)
        when(position){
            0 -> view.setPadding((sidePadding*2),0,sidePadding,0)
            (dataset.size - 1) -> view.setPadding(sidePadding,0,(sidePadding*2),0)
            else -> view.setPadding(sidePadding,0,sidePadding,0)
        }
    }

    fun getViewByPosition(position: Int): View? {
        return holderList[position]
    }

    fun setData(newData: MutableList<String>){
        dataset = newData
        notifyDataSetChanged()
    }

    class ViewHolder(val listItem:CustomImageLayout):RecyclerView.ViewHolder(listItem)
}