package com.example.housingapp.views

import android.content.Context
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import com.example.housingapp.Interfaces.IFilterEventListener
import com.example.housingapp.extras.HelperClass
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.HousingType
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_custom_filter_bar.view.*

class CustomFilterBarLayout(val view: View, context:Context, private val filterEventListener:IFilterEventListener){

    //Initialize the views in the custom filter bar
    private var title:TextView = view.filterBarTitle_textView

    private val filterByPrice:CheckBox = view.filterBar_filterPrice_checkBox
    private val filterByAmenities:CheckBox = view.filterBar_filterAmenities_checkBox
    private val filterByType:CheckBox = view.filterBar_filterType_checkBox

    private val priceTitle:TextView = view.filterBar_priceTitle_textView
    private var fromPrice: EditText = view.filterBar_fromPrice_editText
    private var priceDivider:TextView = view.filterBar_priceDivider_textView
    private var toPrice: EditText = view.filterBar_toPrice_editText
    private var hr3:View = view.horizontalRule3

    private val amenitiesTitle:TextView = view.filterBar_amenitiesTitle_textView
    private val amenitiesTable: TableLayout = view.filterBar_amenities_tableLayout
    private val amenitiesCheckBoxes:MutableList<CheckBox> = mutableListOf()
    private val hr4:View = view.horizontalRule4

    private val housingTypeTitle: TextView = view.filterBar_housingTypeTitle_textView
    private val housingTypeDropDown: Spinner = view.filterBar_housingType_spinner
    private val hr5:View = view.horizontalRule5

    private val addFilterButtonImage: ImageView = view.filterBar_addFilter_imageView
    private val addFilterTitle:TextView = view.filterBar_addFilterTitle_textView
    private val clearFilterButtonImage: ImageView = view.filterBar_clearFilter_imageView
    private val clearFilterTitle:TextView = view.filterBar_clearFilterTitle_textView

    init {
        //Setup listeners to filter by checkboxes
        setClickAndCheckCheckListeners()

        //Use our abstract function to fill the checkboxes needed for the amenities
        HelperClass.setUpTableLayoutFromEnum(context,amenitiesTable,"Checkbox",Amenities.values() as Array<Enum<*>>, 2,amenitiesCheckBoxes)


        //Set icon for the add and clear image views we use
        Picasso.get().load("https://www.freeiconspng.com/uploads/check-mark-31.png").into(addFilterButtonImage)
        Picasso.get().load("https://www.freeiconspng.com/uploads/add-cross-delete-exit-remove-icon--6.png").into(clearFilterButtonImage)
    }

    //Need a way to set the spinner adapter for the housing type drop down.
    fun setDropDownAdapter(adapter: ArrayAdapter<HousingType>){
        housingTypeDropDown.adapter = adapter
    }

    //Create a function to set a listener to the checkboxes to see what user wants to filter by
    private fun setClickAndCheckCheckListeners(){
        filterByPrice.setOnCheckedChangeListener { buttonView, isChecked ->
            setPriceFilter(isChecked)
            showApplyAndClearButtons()
            applyFilters()
        }

        filterByAmenities.setOnCheckedChangeListener { buttonView, isChecked ->
            setAmenitiesFilter(isChecked)
            showApplyAndClearButtons()
            applyFilters()
        }

        filterByType.setOnCheckedChangeListener { buttonView, isChecked ->
            setHousingTypeFilter(isChecked)
            showApplyAndClearButtons()
            applyFilters()
        }

        addFilterButtonImage.setOnClickListener {
            applyFilters()
        }
        clearFilterButtonImage.setOnClickListener {
            clearFilter()
        }
    }

    //Create a function to show the price filters
    private fun setPriceFilter(shouldShow:Boolean){
        priceTitle.isVisible = shouldShow
        fromPrice.isVisible = shouldShow
        priceDivider.isVisible = shouldShow
        toPrice.isVisible = shouldShow
        hr3.isVisible = shouldShow
    }

    //Create a way to clear the price filters
    private fun clearPriceFilter(){
        fromPrice.text = null
        toPrice.text = null
    }

    //Create a way to show the amenities filter
    private fun setAmenitiesFilter(shouldShow: Boolean){
        amenitiesTitle.isVisible = shouldShow
        amenitiesTable.isVisible = shouldShow
        hr4.isVisible = shouldShow
    }

    //Create a way to clear the amenities filter
    private fun clearAmenitiesFilter(){
        for(amenity in amenitiesCheckBoxes){
            amenity.isChecked = false
        }
    }

    //Create a way to show the housing type filter
    private fun setHousingTypeFilter(shouldShow: Boolean){
        housingTypeTitle.isVisible = shouldShow
        housingTypeDropDown.isVisible = shouldShow
        hr5.isVisible = shouldShow
    }

    //Create a way to reset the housing type filter
    private fun resetHousingTypeFilter(){
        housingTypeDropDown.setSelection(0)
    }

    //Create a way to show or hide the apply and clear buttons
    private fun showApplyAndClearButtons(){
        if(filterByPrice.isChecked || filterByAmenities.isChecked || filterByType.isChecked){
            addFilterButtonImage.isVisible = true
            addFilterTitle.isVisible  = true
            clearFilterButtonImage.isVisible = true
            clearFilterTitle.isVisible = true
        } else{
            addFilterButtonImage.isVisible = false
            addFilterTitle.isVisible = false
            clearFilterButtonImage.isVisible = false
            clearFilterTitle.isVisible = false
        }
    }

    //Create a way to apply the filters.
    //Here we will methodically go through all filter items.
    //First set them to null in case they are left empty,
    //or in the case of amenities an empty list.
    //Then check each filter type. If it is chosen, set the item information.
    //When all information is gathered, use the filter event listener interface to
    //send the information to the housing list fragment.
    private fun applyFilters(){

        var priceFrom:Double? = null
        var priceTo:Double? = null
        if(filterByPrice.isChecked){
            priceFrom = fromPrice.text.toString().toDoubleOrNull()
            priceTo = toPrice.text.toString().toDoubleOrNull()
        }

        val amenities = mutableListOf<Amenities>()
        if(filterByAmenities.isChecked){
            for (checkBox in amenitiesCheckBoxes){
                for(amenity in Amenities.values()){
                    if(checkBox.isChecked && checkBox.text.toString().toLowerCase() == amenity.name.toLowerCase()){
                        amenities.add(amenity)
                    }
                }
            }
        }

        var housingType:HousingType? = null
        if(filterByType.isChecked){
            for(type in HousingType.values()){
                if(type.name.toLowerCase() == housingTypeDropDown.selectedItem.toString().toLowerCase()){
                    housingType = type
                }
            }
        }

        filterEventListener.filterEventListener(priceFrom,priceTo,amenities,housingType)
    }

    //Create a way to clear all filters.
    fun clearFilter(){
        filterByPrice.isChecked = false
        filterByAmenities.isChecked = false
        filterByType.isChecked = false

        clearPriceFilter()
        clearAmenitiesFilter()
        resetHousingTypeFilter()

        applyFilters()
    }

}