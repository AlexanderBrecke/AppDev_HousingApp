package com.example.housingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import com.example.housingapp.Interfaces.ICreateNewListingListener
import com.example.housingapp.housing.HousingType
import com.example.housingapp.R
import com.example.housingapp.extras.HelperClass
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.RentPayment
import kotlinx.android.synthetic.main.fragment_housing_add.view.*
import org.w3c.dom.Text

class HousingAddFragment(private val createNewListingListener: ICreateNewListingListener):Fragment() {

    //Setup some values we will use
    private lateinit var housingTypesDropDown:Spinner
    private val housingTypes:MutableList<HousingType> = mutableListOf()

    private lateinit var isRental:CheckBox
    private lateinit var rentpaymentTitle:TextView
    private lateinit var rentPaymentDropDown:Spinner
    private val rentPayments:MutableList<RentPayment> = mutableListOf()

    private lateinit var address:EditText
    private lateinit var price:EditText
    private lateinit var amenitiesTable:TableLayout
    private var amenitiesCheckBoxList = mutableListOf<CheckBox>()
    private var photos:MutableList<EditText> = mutableListOf()

    private lateinit var addButton: Button

    //Initialize the fragment, and all views inside
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing_add,container,false)

        //Initialize what values we can from the view.
        housingTypesDropDown = view.dropDown_spinner
        isRental = view.addHousing_isRental_checkBox
        rentpaymentTitle = view.addHousing_rentPerTitle_textView
        rentPaymentDropDown = view.addHousing_rentPer_spinner
        address = view.address_editText
        price = view.price_editTextNumber
        amenitiesTable = view.amenities_tableLayout

        for(editText in view.imageLinks){
            photos.add(editText as EditText)
        }

        //Use our abstract function to fill the amenities table with checkboxes.
        HelperClass.setUpTableLayoutFromEnum(requireContext(),amenitiesTable,"Checkbox",Amenities.values() as Array<Enum<*>>, 4,amenitiesCheckBoxList)
//        HelperClass.setUpTableLayoutWithCheckboxesFromEnum(requireContext(),amenitiesTable,Amenities.values() as Array<Enum<*>>,4,amenitiesCheckBoxList)

//        setupAmenitiesTable(4)

        addButton = view.add_button

        //add the housing types to the list.
        //We will be using these in the adapter for the spinner
        for(type in HousingType.values()){
            housingTypes.add(type)
        }

        //Add the rent types to the list
        //we will be using these in the spinner adapter
        for(rentType in RentPayment.values()){
            rentPayments.add(rentType)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Setup the adapters for the spinners and set it as the adapter
        val housingTypeDropDownAdapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, housingTypes)
        housingTypesDropDown.adapter = housingTypeDropDownAdapter

        val rentPaymentDropDownAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, rentPayments)
        rentPaymentDropDown.adapter = rentPaymentDropDownAdapter

        //Set an on click listeners
        setClickListeners()
    }

    private fun setClickListeners(){
        isRental.setOnCheckedChangeListener { buttonView, isChecked ->
            rentpaymentTitle.isVisible = isChecked
            rentPaymentDropDown.isVisible = isChecked
        }

        addButton.setOnClickListener {
            tryCreateNewListing()
        }
    }

    //Create a function to clear the input info
    private fun clearInputInfo(){
        housingTypesDropDown.setSelection(0)
        rentPaymentDropDown.setSelection(0)
        isRental.isChecked = false
        address.text = null
        price.text = null
        for(amenityCheckBox in amenitiesCheckBoxList){
            amenityCheckBox.isChecked = false
        }
        for(imageLink in photos){
            imageLink.text = null
        }
    }

    //Create a function to create a new listing.
    private fun tryCreateNewListing(){

        //Setup the needed variables for creating a house listing
        var houseAddress:String? = null
        var housePrice:Double?  = null
        var rentPayment:RentPayment? = null
        var housingType:HousingType = HousingType.Room
        var houseAmenities:MutableList<Amenities> = mutableListOf()
        var housePhotos:MutableList<String> = mutableListOf()

        //initialize them with information if available
        if(address.text.toString() != "") houseAddress = address.text.toString()
        if(price.text.toString() != "") housePrice = price.text.toString().toDouble()

        for (type in housingTypes){
            if(type.name.toLowerCase() == housingTypesDropDown.selectedItem.toString().toLowerCase()){
                housingType = type
            }
        }

        if(isRental.isChecked){
            for(rent in rentPayments){
                if(rent.name.toLowerCase() == rentPaymentDropDown.selectedItem.toString().toLowerCase()){
                    rentPayment = rent
                }
            }
        }

        for(checkBox in amenitiesCheckBoxList){
            if(checkBox.isChecked){
                for(amenity in Amenities.values()){
                    if(amenity.name.toLowerCase() == checkBox.text.toString().toLowerCase()){
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
            clearInputInfo()
            val newHousing = Housing(houseAddress,housePrice,housingType,houseAmenities,housePhotos, rentPayment)
            createNewListingListener.createNewListing(newHousing)
        }
        //If not, tell the user that information is needed
        else Toast.makeText(context,"You need to fill in an address and a price",Toast.LENGTH_SHORT).show()

    }



}