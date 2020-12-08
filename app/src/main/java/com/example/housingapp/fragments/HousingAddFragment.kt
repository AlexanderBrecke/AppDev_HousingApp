package com.example.housingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.example.housingapp.Interfaces.ICreateNewListingListener
import com.example.housingapp.housing.HousingType
import com.example.housingapp.R
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.Housing
import kotlinx.android.synthetic.main.fragment_housing_add.view.*

class HousingAddFragment(private val createNewListingListener: ICreateNewListingListener):Fragment() {

    //Setup some values we will use
    private lateinit var dropDown:Spinner
    private val housingTypes:MutableList<HousingType> = mutableListOf()

    private lateinit var address:EditText
    private lateinit var price:EditText
    private lateinit var amenitiesTable:TableLayout
    private var amenitiesCheckBoxList:MutableList<CheckBox> = mutableListOf()
    private var photos:MutableList<EditText> = mutableListOf()

    private lateinit var addButton: Button

    //Initialize the fragment, and all views inside
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing_add,container,false)

        //Initialize what values we can from the view.
        dropDown = view.dropDown_spinner
        address = view.address_editText
        price = view.price_editTextNumber
        amenitiesTable = view.amenities_tableLayout

        for(editText in view.imageLinks){
            photos.add(editText as EditText)
        }

        setupAmenitiesTable(4)

        addButton = view.add_button

        //add the housing types to the list.
        //We will be using these in the adapter for the spinner
        for(type in HousingType.values()){
            housingTypes.add(type)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup the adapter for the spinner and set it as the adapter
        val dropDownAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, housingTypes)
        dropDown.adapter = dropDownAdapter

        //Set an on click listener on the button
        addButton.setOnClickListener {
            createNewListing()
        }

    }

    //Create function to fill the amenities table with checkboxes.
    //Loop through all amenities in the Amenities enum.
    //If the current index of the loop is the modulo of the max checkboxes per row, create a new table row.
    //Create a checkbox for each amenity in the enum, set the text to be the name of the amenity.
    //Add the checkbox to the amenities checkbox list for easy access.
    //Add the checkbox to the table row for them to show in the UI.
    private fun setupAmenitiesTable(maxCheckBoxesPerRow:Int){
        var tableRow:TableRow? = null
        for(i:Int in Amenities.values().indices){
            if(i % maxCheckBoxesPerRow == 0){
                tableRow= TableRow(context)
                amenitiesTable.addView(tableRow)
            }
            var checkBox:CheckBox = CheckBox(context)
            checkBox.text = Amenities.values()[i].name
            amenitiesCheckBoxList.add(checkBox)

            tableRow?.let{
                tableRow.addView(checkBox)
            }
        }
    }

    //Create a function to create a new listing.
    private fun createNewListing(){

        //Setup the needed variables for creating a house listing
        var houseAddress:String? = null
        var housePrice:Double?  = null
        var housingType:HousingType = HousingType.Room
        var houseAmenities:MutableList<Amenities> = mutableListOf()
        var housePhotos:MutableList<String> = mutableListOf()

        //initialize them with information if available
        if(address.text.toString() != "") houseAddress = address.text.toString()
        if(price.text.toString() != "") housePrice = price.text.toString().toDouble()

        for (type in housingTypes){
            if(type.name.toLowerCase() == dropDown.selectedItem.toString().toLowerCase()){
                housingType = type
            }
        }

        for(checkBox in amenitiesCheckBoxList){
            if(checkBox.isChecked){
                for(amenity in Amenities.values()){
                    if(amenity.name.toString().toLowerCase() == checkBox.text.toString().toLowerCase()){
                        houseAmenities.add(amenity)
                    }
                }
            }
        }

        for(photoLink in photos){
            if(photoLink.text.toString() != ""){
                housePhotos.add(photoLink.text.toString())
            }
        }

        //Check that all information needed is present to create a housing
        if(houseAddress != null && housePrice != null){
            val newHousing = Housing(houseAddress,housePrice,housingType,houseAmenities,housePhotos)
            createNewListingListener.createNewListing(newHousing)
            onDestroy()
        }
        //If not, tell the user that information is needed
        else Toast.makeText(context,"You need to fill in an address and a price",Toast.LENGTH_SHORT).show()

    }



}