package com.example.housingapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.housingapp.R
import com.example.housingapp.extras.HelperClass
import com.example.housingapp.extras.carouselview.CarouselView
import com.example.housingapp.housing.Amenities
import com.example.housingapp.housing.Housing
import com.example.housingapp.housing.RentPayment
import com.example.housingapp.recycler.ImageScrollAdapter
import kotlinx.android.synthetic.main.fragment_housing.view.*

class HousingCurrentFragment(val housing:Housing):Fragment() {

    private lateinit var pictureRecyclerView: CarouselView
    private val pictureRecyclerAdapter:ImageScrollAdapter = ImageScrollAdapter()

    private lateinit var titleView: TextView
    private lateinit var priceView: TextView
    private lateinit var priceRentView:TextView
    private lateinit var typeView:TextView
    private lateinit var amenitiesTable:TableLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_housing,container, false)

        pictureRecyclerView = view.pictures_recycleVIew
        pictureRecyclerView.initialize(pictureRecyclerAdapter)
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

        HelperClass.setUpTableLayoutFromEnum(requireContext(),amenitiesTable,"text view",housing.amenities.toTypedArray() as Array<Enum<*>>, 2)

    }

    private fun convertRentPayment():String{
        var rent:String = ""
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