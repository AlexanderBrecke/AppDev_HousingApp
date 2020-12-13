package com.example.housingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.Interfaces.ICreateNewListingListener
import com.example.housingapp.Interfaces.IFilterEventListener
import com.example.housingapp.housing.Housing
import com.example.housingapp.Interfaces.IRecyclerViewEventListener
import com.example.housingapp.MainActivity
import com.example.housingapp.R
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.HousingType
import com.example.housingapp.recycler.HousingListAdapter
import kotlinx.android.synthetic.main.fragment_housing_list.view.*

class HousingListFragment(private val housings:MutableList<Housing>): Fragment(), IRecyclerViewEventListener, ICreateNewListingListener, IFilterEventListener {

    //Create variables for the recycler view, recycler adapter and recycler layout manager.
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerAdapter:HousingListAdapter
    private lateinit var recyclerLayoutManager: RecyclerView.LayoutManager

    //Create variables for the search bar and add house button
    private lateinit var searchBar:EditText
    private lateinit var  searchBar2:SearchView
    private lateinit var addHousingButton: Button

    //Create a reference housing list that we can manipulate without losing data
    private var referenceHousingList = mutableListOf<Housing>()

    //Create a value for the fragment to add houses
    private val housingAddFragment:HousingAddFragment = HousingAddFragment(this)



    //Initialize the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing_list,container,false)

        recyclerView = view.housing_list
        
        createReferenceHousingList()

        //Initialize search bar and add a way to listen for text change
        searchBar = view.addressSearch_textInputField
        //Improved way of checking for text change without creating a text watcher
        searchBar.addTextChangedListener {
            searchInReferenceList(it.toString())
        }

        //Initialize add button
        addHousingButton = view.addHousing_button
        //Set an on click listener to take you to the add housing fragment
        addHousingButton.setOnClickListener {
            (activity as MainActivity).setFragment(housingAddFragment, true)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    private fun getFilteredHousingsList(fromPrice: Double?, toPrice: Double?, amenities: MutableList<Amenities>, housingType: HousingType?):MutableList<Housing>{
        val updatedList = mutableListOf<Housing>()

        for(housing in housings){
            var shouldAddHousing:Boolean = false
            var fromPriceOk:Boolean = false
            var toPriceOk:Boolean = false
            var housingTypeOk:Boolean = false
            var amenitiesOk:Boolean = true

            if(fromPrice != null){
                if(housing.price >= fromPrice) fromPriceOk =  true
            } else fromPriceOk = true

            if(toPrice != null){
                if(housing.price <= toPrice) toPriceOk = true
            } else toPriceOk = true

            if(housingType != null){
                if(housing.type == housingType) housingTypeOk = true
            } else housingTypeOk = true

            if(amenities.isNotEmpty()){
                for(amenity in amenities){
                    if(!housing.amenities.contains(amenity)){
                        amenitiesOk = false
                        break
                    }
                }
            }

            if(fromPriceOk && toPriceOk && housingTypeOk && amenitiesOk) shouldAddHousing = true

            if(shouldAddHousing){
                updatedList.add(housing)
            }
        }

        return updatedList
    }

    //Create a way to search the reference list for address
    //This should take a query string as an input and create a new filtered list.
    //Loop through the reference list and add all housing objects with address containing the query string to a new list.
    //Set the data set of the recycler adapter to the new list and notify the adapter of the change.
    private fun searchInReferenceList(queryText: String){
        val filteredList = mutableListOf<Housing>()
        for(housing in referenceHousingList){
            if(housing.address.contains(queryText,true)) filteredList.add(housing)
        }
        recyclerAdapter.dataset = filteredList
        recyclerAdapter.notifyDataSetChanged()
    }

    //Create a way to clear any searches
    private fun clearSearch(){
        searchBar.text = null
    }

    //Using the event listener interface for the recycler view override the on cell click listener
    //Here we want to go to a housing fragment showing the information of the clicked housing
    override fun onCellClickListener(housingList: MutableList<Housing>, position: Int) {
        (activity as MainActivity).setFragment(HousingCurrentFragment(housingList[position]),true)
    }

    //Using the event listener interface for the recycler view override the on cell delete click listener.
    //Here, we need to get the information from the adapter of what is being deleted.
    //We need to remove the housing from both the main housings list, as well as the reference housing list.
    //Then search the reference list, as we still want the search to be active even if a housing was deleted.
    override fun onCellDeleteClickListener(housingList: MutableList<Housing>, position: Int) {
        val housingToRemove = housingList[position]
        housings.remove(housingToRemove)
        referenceHousingList.remove(housingToRemove)
        searchInReferenceList(searchBar.text.toString())
        Toast.makeText(context,"Deleted ${housingToRemove.address}", Toast.LENGTH_SHORT).show()
    }

    //Using event listener interface to create new listing from housing add fragment.
    //Here we will add the new listing to the original housings list.
    //Make the function call back press, as it is being called from another fragment and we want to go back to the list fragment at this point.
    //Make it clear any searches in the list fragment.
    override fun createNewListing(newHousing: Housing) {
        Toast.makeText(context,"Got new information about ${newHousing.address}", Toast.LENGTH_SHORT).show()
        housings.add(newHousing)
        (activity as MainActivity).clearFilters()
        (activity as MainActivity).onBackPressed()
        clearSearch()
    }

    //Using filter event listener interface to create a filtered list of housings.
    override fun filterEventListener(fromPrice: Double?, toPrice: Double?, amenities: MutableList<Amenities>, housingType: HousingType?) {
        updateReferenceList(getFilteredHousingsList(fromPrice,toPrice,amenities,housingType))
        searchInReferenceList(searchBar.text.toString())
    }

}