package com.example.housingapp.recycler

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.Housing
import com.example.housingapp.Interfaces.IRecyclerViewEventListener
import com.example.housingapp.views.CustomHousingLayout
import kotlinx.android.synthetic.main.layout_custom_housing.view.*

class HousingListAdapter (
        var dataset: MutableList<Housing>,
        private val recyclerViewEventListener: IRecyclerViewEventListener
): RecyclerView.Adapter<HousingListAdapter.ViewHolder>(){

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val listItem = CustomHousingLayout(context)

        //Describes how a generic cell is supposed to look
        listItem.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        )

        //Then returns it as an instantiated view holder
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //This runs for every item in the data set, apply code here to use the data and show information in each cell
        holder.listItem.setAddress("Address ${dataset[position].address}")
        holder.listItem.setPrice("Price: ${dataset[position].price}")
        val firstImage:String = dataset[position].images[0]
        holder.listItem.setImage(firstImage)

        //Set on click listener on the card and send call to fragment
        holder.listItem.setOnClickListener{
            recyclerViewEventListener.onCellClickListener(dataset,position)
        }

        //Set on click listener on the cross to delete and send call to fragment
        holder.listItem.crossImage.setOnClickListener{
            recyclerViewEventListener.onCellDeleteClickListener(dataset,position)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    //This is the blueprint of a cell to be displayed in the recycler view
    class ViewHolder(val listItem: CustomHousingLayout):RecyclerView.ViewHolder(listItem)
}