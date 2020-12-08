package com.example.housingapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.Interfaces.ICreateNewListingListener
import com.example.housingapp.housing.Housing
import com.example.housingapp.Interfaces.IRecyclerViewEventListener
import com.example.housingapp.MainActivity
import com.example.housingapp.R
import com.example.housingapp.recycler.HousingListAdapter
import kotlinx.android.synthetic.main.fragment_housing_list.view.*

class HousingListFragment(val housings:MutableList<Housing>): Fragment(), IRecyclerViewEventListener, ICreateNewListingListener {

    //Create variables for the recycler view, recycler adapter and recycler layout manager.
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter:RecyclerView.Adapter<*>
    private lateinit var recyclerLayoutManager: RecyclerView.LayoutManager

    //Create variables for the search bar and add house button
    private lateinit var searchBar:EditText
    private lateinit var addHousingButton: Button

    //Create a reference housing list that we can manipulate without losing data
    private val referenceHousingList:MutableList<Housing> = mutableListOf()

    //Create a value for the fragment to add houses
    private val housingAddFragment:HousingAddFragment = HousingAddFragment(this)


    //Initialize the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing_list,container,false)

        recyclerView = view.housing_list
        
        createReferenceHousingList()

        //Need a way to watch for changes in the search bar
        val textWatcher = object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Check if the input is not null
                if(s != null){
                    //Check if the input is not an empty string
                    if(s.toString() != ""){
                        //If it isn't, get a filtered housings list with the input as filter,
                        // and update the reference list to match the filtered list.
                        updateReferenceList(getFilteredHousingsList(s.toString()))
//                        filterHousings(s.toString().toLowerCase())
                    } else {
//                        removeFilter()
                        updateReferenceList(housings)
                    }
                }

            }
        }
        //Initialize search bar and add the text watcher to it
        searchBar = view.addressSearch_textInputField
        searchBar.addTextChangedListener(textWatcher)

        //Initialize add button
        addHousingButton = view.addHousing_button
        //Set an on click listener to take you to the add housing fragment
        addHousingButton.setOnClickListener {
            (activity as MainActivity).showFragment(housingAddFragment, true)
        }

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Code goes here for what to show in the recycler view

        //Assign value to the adapter and layout manager
        recyclerAdapter = HousingListAdapter(referenceHousingList, this)
        recyclerLayoutManager = LinearLayoutManager(activity)

        //Use assigned value in the recycler view
        recyclerView.adapter = recyclerAdapter
        recyclerView.layoutManager = recyclerLayoutManager

    }

    //Fill the reference housing list.
    //Simply go through the main list of housings and add each to the reference list.
    //Make sure to clear the reference list first to make sure it is up to date.
    private fun createReferenceHousingList(){
        referenceHousingList.clear()
        for(house in housings){
            referenceHousingList.add(house)
        }
    }

    //Create a way to update the reference list.
    //Pass an updated list of housings to this function as a parameter.
    //Clear the reference list and go through the updated list and add each housing object to the reference list.
    //Make sure to notify the adapter of the data change.
    private fun updateReferenceList(updatedList: MutableList<Housing>){
        referenceHousingList.clear()
        for (housing in updatedList){
            referenceHousingList.add(housing)
        }
        recyclerAdapter.notifyDataSetChanged()
    }

    //Create a way to get a filtered list of housings.
    //This should take a string as an input, and return a mutable list of housing objects
    //Go through the list of housings and check if the housing address contains the input
    //If it does, add the housing to the filtered list to return
    private fun getFilteredHousingsList(stringToSearchFor: String):MutableList<Housing>{
        var filteredList:MutableList<Housing> = mutableListOf()
        for (housing in housings){
            if(housing.address.toLowerCase().contains(stringToSearchFor.toLowerCase())) filteredList.add(housing)
        }
        return filteredList
    }

    //Create a way to clear any searches
    private fun clearAllSearches(){
        searchBar.text = null
        updateReferenceList(housings)
    }

    //Using the event listener interface for the recycler view override the on cell click listener
    //Here we want to go to a housing fragment showing the information of the clicked housing
    override fun onCellClickListener(housingList: MutableList<Housing>, position: Int) {
        Toast.makeText(context,"Pressed ${housingList[position].address}", Toast.LENGTH_SHORT).show()
    }

    //Using the event listener interface for the recycler view override the on cell delete click listener.
    //Here, we need to get the information from the adapter of what is being deleted.
    //We need to remove the housing from both the main housings list, as well as the reference housing list.
    //Then notify the adapter of the data change.
    override fun onCellDeleteClickListener(housingList: MutableList<Housing>, position: Int) {
        val housingToRemove = housingList[position]
        housings.remove(housingToRemove)
        referenceHousingList.remove(housingToRemove)
        recyclerAdapter.notifyDataSetChanged()
        Toast.makeText(context,"Deleted ${housingToRemove.address}", Toast.LENGTH_SHORT).show()
    }

    //Using event listener interface to create new listing from housing add fragment
    //Add the new listing to the original housings list, and then clear all searches
    //Update the reference list to again show all houses, including the newly added listing.
    //Make the function call the back press, as this is being called in another fragment and we want to go back to the list fragment at this point.
    override fun createNewListing(newHousing: Housing) {
        Toast.makeText(context,"Got new information about ${newHousing.address}", Toast.LENGTH_SHORT).show()
        housings.add(newHousing)
        clearAllSearches()
        (activity as MainActivity).onBackPressed()
    }

}