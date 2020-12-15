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
import com.example.housingapp.extras.carouselview.HorizontalView
import com.example.housingapp.extras.indicator.Indicator
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.RentPayment
import com.example.housingapp.recycler.ImageScrollAdapter
import kotlinx.android.synthetic.main.fragment_housing.view.*

class HousingCurrentFragment(val housing:Housing):Fragment() {

    private lateinit var pictureRecyclerView: HorizontalView
    private val pictureRecyclerAdapter:ImageScrollAdapter = ImageScrollAdapter()

    private lateinit var indicator: Indicator

    private lateinit var imageIndicator:TableRow

    private lateinit var titleView: TextView
    private lateinit var priceView: TextView
    private lateinit var priceRentView:TextView
    private lateinit var typeView:TextView
    private lateinit var amenitiesTable:TableLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing,container, false)


        imageIndicator = view.currentHousing_pictureIndicator_tableRow
        indicator = Indicator(imageIndicator)

        pictureRecyclerView = view.pictures_recycleVIew

        //We will initialize the adapter of the picture recycler view here
        //and set the data of the adapter
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

        titleView.text = housing.address
        priceView.text = "\$ ${housing.price}"
        priceRentView.text = convertRentPayment()
        typeView.text = "${housing.type}"



        //Then we will fill the indicator table of our indicator
        indicator.setDataSize(pictureRecyclerAdapter.itemCount)
        indicator.fillIndicatorTable(getIndicators(indicator.getDataSize(), resources.getDrawable(R.drawable.ic_current_image)))

        //Set up the table of amenities
        HelperClass.setUpTableLayoutFromEnum(requireContext(),amenitiesTable,"text view",housing.amenities.toTypedArray() as Array<Enum<*>>, 2)

    }

    //Make a list of indicators.
    //It should take an input of how many should be in the list
    //as well as a drawable to set as the image of the indicator.
    //Return the list.
    private fun getIndicators(howMany:Int, drawable: Drawable):MutableList<View>{
        val listOfIndicators = mutableListOf<View>()
        for(i:Int in 0 until howMany){
            var indicator = ImageView(context)
            indicator.setImageDrawable(drawable)
            listOfIndicators.add(indicator)
        }
        return listOfIndicators
    }

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