package com.example.housingapp.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.housingapp.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.layout_custom_housing.view.*
import java.io.InputStream

class CustomHousingLayout(context: Context, attributeSet: AttributeSet?=null):FrameLayout(context,attributeSet) {

    private val addressText:TextView
    private val priceText:TextView
    private val imageView:ImageView
    private val crossImageView:ImageView

    init {
        //Defines XML layout for this custom view
        val view = LayoutInflater.from(context).inflate(R.layout.layout_custom_housing, this)

        addressText = view.address_textView
        priceText = view.price_textView
        imageView = view.icon_image
        crossImageView = view.crossImage

        //Set icon to the cross image view
        Picasso.get().load("https://www.freeiconspng.com/uploads/dark-red-x-png-5.png").into(crossImageView)
    }

    //Functions to set the information
    fun setAddress(address:String){
        addressText.text = address
    }

    fun setPrice(price:String){
        priceText.text = price
    }

    fun setImage(imageUrl:String){
        Picasso.get().load(imageUrl).into(imageView)
    }



}