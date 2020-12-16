package com.example.housingapp.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.housingapp.R
import com.example.housingapp.extras.HelperClass
import com.example.housingapp.views.HorizontalView
import com.example.housingapp.extras.indicator.Indicator
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.RentPayment
import com.example.housingapp.recycler.ImageScrollAdapter
import kotlinx.android.synthetic.main.fragment_housing.view.*

class HousingCurrentFragment(val housing:Housing):Fragment() {

    //Setup some variables we will use in the fragment.
    private lateinit var imageIndicator:TableRow
    private lateinit var indicator: Indicator

    private lateinit var pictureRecyclerView: HorizontalView
    private val pictureRecyclerAdapter:ImageScrollAdapter = ImageScrollAdapter()

    private lateinit var titleView: TextView
    private lateinit var priceView: TextView
    private lateinit var priceRentView:TextView
    private lateinit var typeView:TextView
    private lateinit var amenitiesTable:TableLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing,container, false)

        //Initialize what we can from having access to the fragment view.
        imageIndicator = view.currentHousing_pictureIndicator_tableRow
        indicator = Indicator(imageIndicator)

        pictureRecyclerView = view.pictures_recycleVIew
        pictureRecyclerView.initialize(pictureRecyclerAdapter, indicator)
        pictureRecyclerAdapter.setData(housing.images)

        titleView = view.currentHousing_title_textView
        priceView = view.currentHousing_price_textView
        priceRentView = view.currentHousing_priceRent_textView
        typeView = view.currentHousing_housingType_textView
        amenitiesTable = view.currentHousing_amenitiesTable_tableLayout

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Using the Housing object, we will set the data to the respective view.
        titleView.text = housing.address
        priceView.text = "\$ ${housing.price}"
        priceRentView.text = convertRentPayment()
        typeView.text = "${housing.type}"

        //Use the abstract class from the Helper class to set up the table of amenities
        HelperClass.setUpTableLayoutFromEnum(requireContext(),amenitiesTable,"text view",housing.amenities.toTypedArray() as Array<Enum<*>>, 2)

        //We will set the data size of our indicators to be the item count of our recycler adapter.
        //Then fill the indicator table with icons from resources.
        indicator.setDataSize(pictureRecyclerAdapter.itemCount)
        indicator.fillIndicatorTable(getIndicators(indicator.getDataSize(), resources.getDrawable(R.drawable.ic_current_image)))
    }

    //We need a function to make a list of indicators.
    //It should take an input of how many should be in the list
    //as well as a drawable to set as the image of the indicator.
    //Make a list of indicators
    //Loop as many as input, and create an image view and set the drawable then add it to the list.
    //Return the list
    private fun getIndicators(howMany:Int, drawable: Drawable):MutableList<View>{
        val listOfIndicators = mutableListOf<View>()
        for(i:Int in 0 until howMany){
            var indicator = ImageView(context)
            indicator.setImageDrawable(drawable)
            listOfIndicators.add(indicator)
        }
        return listOfIndicators
    }

    //As we do not always use the value of the enum as the information to show,
    // we need a way to convert the rent payment to strings.
    //Rather, we will use a list of strings in the same order as the enum.
    //If the housing uses the rent payment enum, we will loop through our list of strings
    //and return the string with the same index of the enum being used.
    private fun convertRentPayment():String{
        var rent = ""
        var rentTypes = listOf(" / night", " / weekend", " / week", " / two weeks", " / month", " / year")
        if(housing.rentPayment != null){
            for(i:Int in RentPayment.values().indices){
                if(housing.rentPayment == RentPayment.values()[i]){
                    rent = rentTypes[i]
                    return rent
                }
            }
        }
        return rent
    }

}